package com.plotori.develop.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Weekly featured submission spotlight")
public class WinnerSpotlightResponse {
    @Schema(description = "Week date", example = "2026-05-25")
    private LocalDate weekOf;

    @Schema(description = "Submission ID", example = "42")
    private Long submissionId;

    @Schema(description = "Submission title", example = "Othello's Redemption")
    private String title;

    @Schema(description = "Author username", example = "alice_writer")
    private String authorUsername;

    @Schema(description = "Original text", example = "Othello")
    private String textTitle;

    @Schema(description = "Final composite score", example = "4.75")
    private Double totalScore;

    @Schema(description = "Admin editorial note", example = "Exceptional grasp of Iago's psychology...")
    private String editorialNote;
}
