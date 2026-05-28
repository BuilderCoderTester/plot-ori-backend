package com.plotori.develop.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull
    private Long submissionId;

    @Min(1) @Max(5)
    private Integer characterFidelity;

    @Min(1) @Max(5)
    private Integer textualIntelligence;

    @Min(1) @Max(5)
    private Integer creativeOriginality;

    @Min(1) @Max(5)
    private Integer stylisticCraft;

    @Min(1) @Max(5)
    private Integer interpretiveInsight;

    private String comment;
}
