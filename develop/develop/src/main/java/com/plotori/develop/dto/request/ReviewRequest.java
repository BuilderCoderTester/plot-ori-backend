package com.plotori.develop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Structured peer review with 5-part rubric")
public class ReviewRequest {
    @Schema(hidden = true)
    @NotNull
    private Long submissionId;

    @Schema(description = "How well characters stay true to original", example = "4", minimum = "1", maximum = "5")
    @Min(1) @Max(5)
    private Integer characterFidelity;

    @Schema(description = "Understanding of source text", example = "5", minimum = "1", maximum = "5")
    @Min(1) @Max(5)
    private Integer textualIntelligence;

    @Schema(description = "Originality of the creative approach", example = "5", minimum = "1", maximum = "5")
    @Min(1) @Max(5)
    private Integer creativeOriginality;

    @Schema(description = "Quality of writing style", example = "4", minimum = "1", maximum = "5")
    @Min(1) @Max(5)
    private Integer stylisticCraft;

    @Schema(description = "Depth of thematic insight", example = "5", minimum = "1", maximum = "5")
    @Min(1) @Max(5)
    private Integer interpretiveInsight;

    @Schema(description = "Structured comment: captured well / suggestion / question", example = "One thing captured well: the emotional weight of betrayal...")
    private String comment;
}
