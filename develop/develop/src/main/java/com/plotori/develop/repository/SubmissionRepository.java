package com.plotori.develop.repository;
import com.plotori.develop.domain.entity.Submission;
import com.plotori.develop.domain.enums.SubmissionState;
import com.plotori.develop.domain.enums.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUserId(Long userId);

    List<Submission> findByTextIdAndStatus(Long textId, SubmissionStatus status);

    List<Submission> findByStatus(SubmissionStatus status);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.submission.id = :submissionId")
    Long countVotesBySubmissionId(@Param("submissionId") Long submissionId);
    // Phase 2 additions:
    List<Submission> findByWeeklyPromptIdAndState(Long weeklyPromptId, SubmissionState state);

    @Query("SELECT s FROM Submission s WHERE s.weeklyPrompt.id = :promptId AND s.state = 'SUBMITTED' ORDER BY s.totalScore DESC")
    List<Submission> findTopByWeeklyPromptOrderByScore(@Param("promptId") Long promptId);

}
