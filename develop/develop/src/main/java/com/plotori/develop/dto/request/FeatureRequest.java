package com.plotori.develop.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
@Schema(description = "Admin feature selection with editorial note")
public class FeatureRequest {

    @Schema(description = "Submission ID to feature", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long submissionId;

    @Schema(description = "Editorial note explaining why featured", example = "Exceptional grasp of Iago's psychology...")
    private String editorialNote;
}
