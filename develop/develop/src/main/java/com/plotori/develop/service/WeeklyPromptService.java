package com.plotori.develop.service;

import com.plotori.develop.domain.entity.Text;
import com.plotori.develop.dto.response.WeeklyPromptResponse;
import com.plotori.develop.repository.TextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyPromptService {
    private final TextRepository textRepository;

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
