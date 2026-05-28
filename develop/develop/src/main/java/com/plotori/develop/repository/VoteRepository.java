package com.plotori.develop.repository;

import com.plotori.develop.domain.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserIdAndSubmissionId(Long userId, Long submissionId);
    boolean existsByUserIdAndSubmissionId(Long userId, Long submissionId);
    long countBySubmissionId(Long submissionId);
}
