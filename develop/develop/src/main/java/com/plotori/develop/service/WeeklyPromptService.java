package com.plotori.develop.service;

import com.plotori.develop.domain.entity.*;
import com.plotori.develop.domain.enums.PromptStatus;
import com.plotori.develop.domain.enums.SubmissionState;
import com.plotori.develop.dto.response.WeeklyPromptResponse;
import com.plotori.develop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeeklyPromptService {
    private final TextRepository textRepository;
    private final WeeklyPromptRepository weeklyPromptRepository;
    private final PromptVoteRepository promptVoteRepository;
    private final SubmissionRepository submissionRepository;
    private final ArchiveEntryRepository archiveEntryRepository;
    private final LeaderboardEntryRepository leaderboardEntryRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    public WeeklyPromptResponse getCurrentWeeklyPrompt() {
        // Get all canonical texts and rotate weekly based on week number
        List<Text> canonicalTexts = textRepository.findByCanonicalStatus("CANONICAL");

        if (canonicalTexts.isEmpty()) {
            throw new RuntimeException("No canonical texts available");
        }

        int weekOfYear = LocalDate.now().get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
        Text currentText = canonicalTexts.get(weekOfYear % canonicalTexts.size());

        LocalDate weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = weekStart.plusDays(6);

        return WeeklyPromptResponse.builder()
                .textId(currentText.getId())
                .textTitle(currentText.getTitle())
                .textAuthor(currentText.getAuthor())
                .promptDescription(buildPromptDescription(currentText))
                .weekStart(weekStart)
                .weekEnd(weekEnd)
                .build();
    }
    // ==================== VOTING PHASE (Sat-Sun) ====================

    @Transactional
    public void startVotingPhase() {
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate nextSunday = nextMonday.minusDays(1);

        // Create voting prompt with all canonical texts as candidates
        WeeklyPrompt votingPrompt = WeeklyPrompt.builder()
                .text(null)  // Will be set after voting
                .weekStart(nextMonday)
                .weekEnd(nextMonday.plusDays(6))
                .description("Community voting open! Choose next week's text.")
                .status(PromptStatus.VOTING)
                .votingDeadline(nextSunday.atTime(23, 59))
                .submissionDeadline(nextMonday.plusDays(6).atTime(23, 59))
                .build();

        weeklyPromptRepository.save(votingPrompt);
        log.info("Voting phase started for week of {}", nextMonday);
    }

    @Transactional
    public void castVote(Long userId, Long textId) {
        WeeklyPrompt votingPrompt = weeklyPromptRepository.findByStatus(PromptStatus.VOTING)
                .orElseThrow(() -> new RuntimeException("No voting phase currently active"));

        if (promptVoteRepository.existsByUserIdAndWeeklyPromptId(userId, votingPrompt.getId())) {
            throw new RuntimeException("You have already voted for this week");
        }

        Text votedText = textRepository.findById(textId)
                .orElseThrow(() -> new RuntimeException("Text not found"));

        PromptVote vote = PromptVote.builder()
                .user(userRepository.findById(userId).orElseThrow())
                .weeklyPrompt(votingPrompt)
                .votedText(votedText)
                .build();

        promptVoteRepository.save(vote);
        log.info("User {} voted for text {}", userId, textId);
    }

    @Transactional(readOnly = true)
    public List<VoteResult> getVotingResults() {
        WeeklyPrompt votingPrompt = weeklyPromptRepository.findByStatus(PromptStatus.VOTING)
                .orElseThrow(() -> new RuntimeException("No voting phase active"));

        List<Object[]> results = promptVoteRepository.countVotesByTextForPrompt(votingPrompt.getId());

        return results.stream()
                .map(r -> new VoteResult((Long) r[0], (Long) r[1]))
                .toList();
    }

    public record VoteResult(Long textId, Long voteCount) {}

    // ==================== ACTIVATION (Monday 00:00) ====================

    @Transactional
    public void activatePrompt() {
        WeeklyPrompt votingPrompt = weeklyPromptRepository.findByStatus(PromptStatus.VOTING)
                .orElseThrow(() -> new RuntimeException("No prompt in voting phase"));

        // Tally votes and pick winner
        List<Object[]> voteCounts = promptVoteRepository.countVotesByTextForPrompt(votingPrompt.getId());

        if (voteCounts.isEmpty()) {
            // No votes — default to first canonical text
            Text defaultText = textRepository.findByCanonicalStatus("CANONICAL")
                    .stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("No canonical texts available"));
            votingPrompt.setText(defaultText);
        } else {
            Long winningTextId = (Long) voteCounts.get(0)[0];
            Text winner = textRepository.findById(winningTextId)
                    .orElseThrow(() -> new RuntimeException("Winning text not found"));
            votingPrompt.setText(winner);
        }

        votingPrompt.setStatus(PromptStatus.ACTIVE);
        votingPrompt.setDescription(buildPromptDescription(votingPrompt.getText()));

        weeklyPromptRepository.save(votingPrompt);
        log.info("Prompt activated for week {}: {}", votingPrompt.getWeekStart(), votingPrompt.getText().getTitle());
    }

    // ==================== REVIEW PHASE (Saturday) ====================

    @Transactional
    public void startReviewPhase() {
        WeeklyPrompt activePrompt = weeklyPromptRepository.findByStatus(PromptStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active prompt"));

        activePrompt.setStatus(PromptStatus.REVIEWING);
        weeklyPromptRepository.save(activePrompt);

        // Auto-archive all submitted submissions
        List<Submission> submissions = submissionRepository.findByWeeklyPromptIdAndState(
                activePrompt.getId(), SubmissionState.SUBMITTED);

        for (Submission sub : submissions) {
            // Calculate rubric average
            Double rubricAvg = calculateRubricAverage(sub.getId());
            sub.setRubricAverage(rubricAvg);

            // Calculate total score: 50% rubric + 30% votes + 20% admin (admin added later)
            Long voteCount = submissionRepository.countVotesBySubmissionId(sub.getId());
            Double normalizedVotes = Math.min(voteCount / 10.0, 5.0); // Cap at 5.0
            Double totalScore = (rubricAvg * 0.5) + (normalizedVotes * 0.3);
            sub.setTotalScore(totalScore);

            // Auto-archive
            ArchiveEntry archive = ArchiveEntry.builder()
                    .submission(sub)
                    .tags(sub.getType().name() + ", " + sub.getText().getGenre())
                    .featured(false)
                    .build();
            archiveEntryRepository.save(archive);

            // Update leaderboard
            updateLeaderboard(sub.getUser(), sub);
        }

        weeklyPromptRepository.save(activePrompt);
        log.info("Review phase started for week {}", activePrompt.getWeekStart());
    }

    @Transactional
    public void featureSubmission(Long submissionId, String editorialNote) {
        WeeklyPrompt reviewingPrompt = weeklyPromptRepository.findByStatus(PromptStatus.REVIEWING)
                .orElseThrow(() -> new RuntimeException("No prompt in review phase"));

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        // Add admin editorial discretion (20% weight)
        Double currentScore = submission.getTotalScore();
        Double finalScore = currentScore + (5.0 * 0.2); // Admin max boost
        submission.setTotalScore(finalScore);
        submission.setState(SubmissionState.FEATURED);

        // Update archive entry
        ArchiveEntry archive = archiveEntryRepository.findBySubmissionId(submissionId)
                .orElseThrow(() -> new RuntimeException("Archive entry not found"));
        archive.setFeatured(true);
        archive.setEditorialNote(editorialNote);

        reviewingPrompt.setFeaturedSubmission(submission);
        reviewingPrompt.setStatus(PromptStatus.ARCHIVED);

        submissionRepository.save(submission);
        archiveEntryRepository.save(archive);
        weeklyPromptRepository.save(reviewingPrompt);

        log.info("Featured submission {} for week {}", submissionId, reviewingPrompt.getWeekStart());
    }

    // ==================== ARCHIVE ====================

    @Transactional
    public void archivePrompt() {
        WeeklyPrompt reviewingPrompt = weeklyPromptRepository.findByStatus(PromptStatus.REVIEWING)
                .orElseThrow(() -> new RuntimeException("No prompt in review phase"));

        // If no featured submission picked, auto-pick highest score
        if (reviewingPrompt.getFeaturedSubmission() == null) {
            autoSelectFeatured(reviewingPrompt);
        }

        reviewingPrompt.setStatus(PromptStatus.ARCHIVED);
        weeklyPromptRepository.save(reviewingPrompt);
        log.info("Prompt archived for week {}", reviewingPrompt.getWeekStart());
    }

    // ==================== HELPERS ====================


    private Double calculateRubricAverage(Long submissionId) {
        List<Review> reviews = reviewRepository.findBySubmissionId(submissionId);
        if (reviews.isEmpty()) return 0.0;

        double sum = reviews.stream()
                .mapToInt(r ->
                        (r.getCharacterFidelity() != null ? r.getCharacterFidelity() : 0) +
                                (r.getTextualIntelligence() != null ? r.getTextualIntelligence() : 0) +
                                (r.getCreativeOriginality() != null ? r.getCreativeOriginality() : 0) +
                                (r.getStylisticCraft() != null ? r.getStylisticCraft() : 0) +
                                (r.getInterpretiveInsight() != null ? r.getInterpretiveInsight() : 0)
                )
                .sum();

        return sum / (reviews.size() * 5.0); // Normalize to 0-5 scale
    }

    private void autoSelectFeatured(WeeklyPrompt prompt) {
        List<Submission> candidates = submissionRepository
                .findByWeeklyPromptIdAndState(prompt.getId(), SubmissionState.SUBMITTED);

        Submission winner = candidates.stream()
                .max((a, b) -> Double.compare(
                        a.getTotalScore() != null ? a.getTotalScore() : 0,
                        b.getTotalScore() != null ? b.getTotalScore() : 0))
                .orElse(null);

        if (winner != null) {
            winner.setState(SubmissionState.FEATURED);
            prompt.setFeaturedSubmission(winner);
            submissionRepository.save(winner);
        }
    }

    private void updateLeaderboard(User user, Submission submission) {
        LocalDateTime weekStart = submission.getWeeklyPrompt().getWeekStart().atStartOfDay();

        LeaderboardEntry entry = leaderboardEntryRepository
                .findByUserIdAndWeekOf(user.getId(), weekStart)
                .orElseGet(() -> LeaderboardEntry.builder()
                        .user(user)
                        .weekOf(weekStart)
                        .build());

        entry.setSubmissionsCount(entry.getSubmissionsCount() + 1);
        entry.setWeeklyStreak(entry.getWeeklyStreak() + 1);

        // Genre mastery tracking
        String author = submission.getText().getAuthor();
        if (author != null && author.toLowerCase().contains("shakespeare")) {
            entry.setGenreMasteryShakespeare(entry.getGenreMasteryShakespeare() + 1);
        } else if (author != null && author.toLowerCase().contains("brontë")) {
            entry.setGenreMasteryBronte(entry.getGenreMasteryBronte() + 1);
        }

        leaderboardEntryRepository.save(entry);
    }

    private WeeklyPromptResponse mapToResponse(WeeklyPrompt prompt) {
        return WeeklyPromptResponse.builder()
                .promptId(prompt.getId())
                .textId(prompt.getText() != null ? prompt.getText().getId() : null)
                .textTitle(prompt.getText() != null ? prompt.getText().getTitle() : "Voting in progress...")
                .textAuthor(prompt.getText() != null ? prompt.getText().getAuthor() : null)
                .promptDescription(prompt.getDescription())
                .weekStart(prompt.getWeekStart())
                .weekEnd(prompt.getWeekEnd())
                .status(prompt.getStatus().name())
                .votingDeadline(prompt.getVotingDeadline())
                .submissionDeadline(prompt.getSubmissionDeadline())
                .featuredSubmissionId(prompt.getFeaturedSubmission() != null ? prompt.getFeaturedSubmission().getId() : null)
                .build();
    }
    private String buildPromptDescription(Text text) {
        return String.format(
                "This week, reimagine '%s' by %s. " +
                        "Write an alternative ending, explore a counterfactual scenario, " +
                        "or rewrite from a villain's perspective. " +
                        "Submissions open Tuesday through Friday.",
                text.getTitle(),
                text.getAuthor()
        );
    }
}
