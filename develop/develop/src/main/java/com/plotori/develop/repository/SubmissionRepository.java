package com.plotori.develop.repository;
import com.plotori.develop.domain.entity.Submission;
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
}
