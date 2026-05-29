package com.plotori.develop.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Weekly writing prompt with lifecycle status")
public class WeeklyPromptResponse {

    @Schema(description = "Prompt ID", example = "1")
    private Long promptId;

    @Schema(description = "Selected text ID", example = "1")
    private Long textId;

    @Schema(description = "Text title", example = "Othello")
    private String textTitle;

    @Schema(description = "Text author", example = "William Shakespeare")
    private String textAuthor;

    @Schema(description = "Prompt description with rules", example = "This week, reimagine 'Othello'...")
    private String promptDescription;

    @Schema(description = "Week start date", example = "2026-06-01")
    private LocalDate weekStart;

    @Schema(description = "Week end date", example = "2026-06-07")
    private LocalDate weekEnd;

    @Schema(description = "Current status", example = "ACTIVE", allowableValues = {"VOTING", "ACTIVE", "REVIEWING", "ARCHIVED"})
    private String status;

    @Schema(description = "Voting deadline")
    private LocalDateTime votingDeadline;

    @Schema(description = "Submission deadline")
    private LocalDateTime submissionDeadline;

    @Schema(description = "Featured submission ID", example = "42")
    private Long featuredSubmissionId;
}
