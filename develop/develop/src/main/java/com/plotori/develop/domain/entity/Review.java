package com.plotori.develop.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    // Five-part rubric (1-5 scale each)
    @Column(name = "character_fidelity")
    private Integer characterFidelity;

    @Column(name = "textual_intelligence")
    private Integer textualIntelligence;

    @Column(name = "creative_originality")
    private Integer creativeOriginality;

    @Column(name = "stylistic_craft")
    private Integer stylisticCraft;

    @Column(name = "interpretive_insight")
    private Integer interpretiveInsight;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
