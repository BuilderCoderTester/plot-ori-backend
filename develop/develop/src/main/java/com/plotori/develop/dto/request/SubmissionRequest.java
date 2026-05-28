package com.plotori.develop.dto.request;

import com.plotori.develop.domain.enums.SubmissionStatus;
import com.plotori.develop.domain.enums.SubmissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Creative writing submission payload")
public class SubmissionRequest {
    @Schema(description = "ID of the canonical text being reimagined", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long textId;

    @Schema(description = "Type of creative piece", example = "ALTERNATIVE_ENDING", allowableValues = {
            "ALTERNATIVE_ENDING", "COUNTERFACTUAL", "VILLAIN_REVISION", "CHARACTER_JOURNAL", "DIALOGUE_REWRITE"
    }, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private SubmissionType type;

    @Schema(description = "Title of your piece", example = "Othello's Redemption", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String title;

    @Schema(description = "Full creative content", example = "Desdemona stirred...", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String content;

    @Schema(description = "Draft or final submission", example = "DRAFT", allowableValues = {"DRAFT", "SUBMITTED"})
    private SubmissionStatus status = SubmissionStatus.DRAFT;
}
