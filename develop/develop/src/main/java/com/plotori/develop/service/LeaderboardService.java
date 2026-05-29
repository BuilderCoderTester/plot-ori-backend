package com.plotori.develop.service;

import com.plotori.develop.domain.entity.LeaderboardEntry;
import com.plotori.develop.domain.entity.Review;
import com.plotori.develop.domain.entity.User;
import com.plotori.develop.domain.enums.PromptStatus;
import com.plotori.develop.dto.response.*;
import com.plotori.develop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final LeaderboardEntryRepository leaderboardEntryRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final SubmissionRepository submissionRepository;
    private final WeeklyPromptRepository weeklyPromptRepository;
    private final ArchiveEntryRepository archiveEntryRepository;

    // ==================== WEEKLY WINNER SPOTLIGHT ====================

    @Transactional(readOnly = true)
    public WinnerSpotlightResponse getWeeklyWinnerSpotlight() {
        var latestArchived = weeklyPromptRepository
                .findByStatusOrderByWeekStartDesc(PromptStatus.ARCHIVED)
                .stream().findFirst()
                .orElse(null);

        if (latestArchived == null || latestArchived.getFeaturedSubmission() == null) {
            return null;
        }

        var winner = latestArchived.getFeaturedSubmission();
        var archive = archiveEntryRepository.findBySubmissionId(winner.getId()).orElse(null);

        return WinnerSpotlightResponse.builder()
                .weekOf(latestArchived.getWeekStart())
                .submissionId(winner.getId())
                .title(winner.getTitle())
                .authorUsername(winner.getUser().getUsername())
                .textTitle(winner.getText().getTitle())
                .totalScore(winner.getTotalScore())
                .editorialNote(archive != null ? archive.getEditorialNote() : null)
                .build();
    }

    // ==================== TOP REVIEWERS ====================

    @Transactional(readOnly = true)
    public List<TopReviewerResponse> getTopReviewers() {
        return leaderboardEntryRepository.findTop10ByOrderByReviewsGivenDesc()
                .stream()
                .map(entry -> TopReviewerResponse.builder()
                        .userId(entry.getUser().getId())
                        .username(entry.getUser().getUsername())
                        .reviewsGiven(entry.getReviewsGiven())
                        .reviewsReceivedHelpful(entry.getReviewsReceivedHelpful())
                        .build())
                .collect(Collectors.toList());
    }

    // ==================== STREAK LEADERS ====================

    @Transactional(readOnly = true)
    public List<StreakLeaderResponse> getStreakLeaders() {
        return leaderboardEntryRepository.findTop10ByOrderByWeeklyStreakDesc()
                .stream()
                .map(entry -> StreakLeaderResponse.builder()
                        .userId(entry.getUser().getId())
                        .username(entry.getUser().getUsername())
                        .weeklyStreak(entry.getWeeklyStreak())
                        .submissionsCount(entry.getSubmissionsCount())
                        .build())
                .collect(Collectors.toList());
    }

    // ==================== GENRE MASTERY ====================

    @Transactional(readOnly = true)
    public List<GenreMasterResponse> getGenreMasters() {
        List<LeaderboardEntry> allEntries = leaderboardEntryRepository.findAll();

        // Aggregate by user across all weeks
        return allEntries.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getUser().getId(),
                        Collectors.reducing((a, b) -> {
                            LeaderboardEntry merged = new LeaderboardEntry();
                            merged.setUser(a.getUser());
                            merged.setGenreMasteryShakespeare(
                                    a.getGenreMasteryShakespeare() + b.getGenreMasteryShakespeare());
                            merged.setGenreMasteryBronte(
                                    a.getGenreMasteryBronte() + b.getGenreMasteryBronte());
                            return merged;
                        })
                ))
                .values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparingInt(
                                (LeaderboardEntry e) -> e.getGenreMasteryShakespeare() + e.getGenreMasteryBronte())
                        .reversed())
                .limit(10)
                .map(entry -> GenreMasterResponse.builder()
                        .userId(entry.getUser().getId())
                        .username(entry.getUser().getUsername())
                        .shakespeareCount(entry.getGenreMasteryShakespeare())
                        .bronteCount(entry.getGenreMasteryBronte())
                        .totalMastery(entry.getGenreMasteryShakespeare() + entry.getGenreMasteryBronte())
                        .build())
                .collect(Collectors.toList());
    }

    // ==================== USER PROFILE STATS ====================

    @Transactional(readOnly = true)
    public UserStatsResponse getUserStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<LeaderboardEntry> entries = leaderboardEntryRepository.findAll()
                .stream()
                .filter(e -> e.getUser().getId().equals(userId))
                .toList();

        int totalSubmissions = entries.stream()
                .mapToInt(LeaderboardEntry::getSubmissionsCount).sum();
        int totalReviewsGiven = entries.stream()
                .mapToInt(LeaderboardEntry::getReviewsGiven).sum();
        int currentStreak = entries.stream()
                .mapToInt(LeaderboardEntry::getWeeklyStreak)
                .max().orElse(0);
        int shakespeareMastery = entries.stream()
                .mapToInt(LeaderboardEntry::getGenreMasteryShakespeare).sum();
        int bronteMastery = entries.stream()
                .mapToInt(LeaderboardEntry::getGenreMasteryBronte).sum();

        // Count featured submissions
        long featuredCount = submissionRepository.findByUserId(userId).stream()
                .filter(s -> s.getState() == com.plotori.develop.domain.enums.SubmissionState.FEATURED)
                .count();

        return UserStatsResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .totalSubmissions(totalSubmissions)
                .totalReviewsGiven(totalReviewsGiven)
                .currentStreak(currentStreak)
                .featuredSubmissions((int) featuredCount)
                .shakespeareMastery(shakespeareMastery)
                .bronteMastery(bronteMastery)
                .build();
    }

    // ==================== EVENT TRACKERS (called by other services) ====================

    @Transactional
    public void trackReviewGiven(Long reviewerId) {
        LeaderboardEntry entry = getOrCreateCurrentWeekEntry(reviewerId);
        entry.setReviewsGiven(entry.getReviewsGiven() + 1);
        leaderboardEntryRepository.save(entry);
    }

    @Transactional
    public void trackReviewHelpful(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        LeaderboardEntry entry = getOrCreateCurrentWeekEntry(review.getReviewer().getId());
        entry.setReviewsReceivedHelpful(entry.getReviewsReceivedHelpful() + 1);
        leaderboardEntryRepository.save(entry);
    }

    // ==================== HELPERS ====================

    private LeaderboardEntry getOrCreateCurrentWeekEntry(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime weekStart = LocalDate.now()
                .with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
                .atStartOfDay();

        return leaderboardEntryRepository
                .findByUserIdAndWeekOf(userId, weekStart)
                .orElseGet(() -> {
                    LeaderboardEntry newEntry = LeaderboardEntry.builder()
                            .user(user)
                            .weekOf(weekStart)
                            .build();
                    return leaderboardEntryRepository.save(newEntry);
                });
    }
}
