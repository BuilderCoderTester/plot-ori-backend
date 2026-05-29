package com.plotori.develop.domain.entity;


import com.plotori.develop.domain.enums.SubmissionState;
import com.plotori.develop.domain.enums.SubmissionStatus;
import com.plotori.develop.domain.enums.SubmissionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_id", nullable = false)
    private Text text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionType type;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SubmissionStatus status = SubmissionStatus.DRAFT;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Review> reviews = new HashSet<>();

    @OneToOne(mappedBy = "submission", cascade = CascadeType.ALL)
    private ArchiveEntry archiveEntry;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_prompt_id")
    private WeeklyPrompt weeklyPrompt;

    @Column(name = "rubric_average")
    private Double rubricAverage;  // Auto-calculated from reviews

    @Column(name = "total_score")
    private Double totalScore;     // 50% rubric + 30% votes + 20% admin

    private SubmissionState state;

}
