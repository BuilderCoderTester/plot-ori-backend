package com.plotori.develop.repository;
import com.plotori.develop.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findBySubmissionId(Long submissionId);
    boolean existsBySubmissionIdAndReviewerId(Long submissionId, Long reviewerId);
}
