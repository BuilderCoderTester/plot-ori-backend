package com.plotori.develop.repository;
import com.plotori.develop.domain.entity.WeeklyPrompt;
import com.plotori.develop.domain.enums.PromptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyPromptRepository extends JpaRepository<WeeklyPrompt, Long> {
    Optional<WeeklyPrompt> findByStatus(PromptStatus status);
    Optional<WeeklyPrompt> findByWeekStart(LocalDate weekStart);
    List<WeeklyPrompt> findByStatusOrderByWeekStartDesc(PromptStatus status);
    List<WeeklyPrompt> findAllByOrderByWeekStartDesc();
}
