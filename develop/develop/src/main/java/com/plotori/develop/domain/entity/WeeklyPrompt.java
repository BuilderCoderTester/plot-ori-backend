package com.plotori.develop.domain.entity;
import com.plotori.develop.domain.enums.PromptStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "weekly_prompts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyPrompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_id", nullable = false)
    private Text text;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @Column(name = "week_end", nullable = false)
    private LocalDate weekEnd;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PromptStatus status = PromptStatus.VOTING;

    @Column(name = "voting_deadline")
    private LocalDateTime votingDeadline;

    @Column(name = "submission_deadline")
    private LocalDateTime submissionDeadline;

    @OneToMany(mappedBy = "weeklyPrompt", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Submission> submissions = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featured_submission_id")
    private Submission featuredSubmission;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
