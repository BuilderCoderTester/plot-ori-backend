package com.plotori.develop.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboard_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "week_of")
    private LocalDateTime weekOf;

    @Column(name = "reviews_given")
    @Builder.Default
    private Integer reviewsGiven = 0;

    @Column(name = "reviews_received_helpful")
    @Builder.Default
    private Integer reviewsReceivedHelpful = 0;

    @Column(name = "submissions_count")
    @Builder.Default
    private Integer submissionsCount = 0;

    @Column(name = "weekly_streak")
    @Builder.Default
    private Integer weeklyStreak = 0;

    @Column(name = "genre_mastery_shakespeare")
    @Builder.Default
    private Integer genreMasteryShakespeare = 0;

    @Column(name = "genre_mastery_bronte")
    @Builder.Default
    private Integer genreMasteryBronte = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
