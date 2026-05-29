package com.plotori.develop.repository;
import com.plotori.develop.domain.entity.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardEntryRepository  extends JpaRepository<LeaderboardEntry, Long> {
    Optional<LeaderboardEntry> findByUserIdAndWeekOf(Long userId, LocalDateTime weekOf);
    List<LeaderboardEntry> findTop10ByOrderByReviewsGivenDesc();
    List<LeaderboardEntry> findTop10ByOrderByWeeklyStreakDesc();
    List<LeaderboardEntry> findAll();
}
