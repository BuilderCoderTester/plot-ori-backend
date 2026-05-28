package com.plotori.develop.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String reviewerUsername;
    private Integer characterFidelity;
    private Integer textualIntelligence;
    private Integer creativeOriginality;
    private Integer stylisticCraft;
    private Integer interpretiveInsight;
    private String comment;
    private LocalDateTime createdAt;
}
