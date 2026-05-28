package com.plotori.develop.service;


import com.plotori.develop.domain.entity.Review;
import com.plotori.develop.domain.entity.Submission;
import com.plotori.develop.domain.entity.User;
import com.plotori.develop.dto.request.ReviewRequest;
import com.plotori.develop.dto.response.ReviewResponse;
import com.plotori.develop.repository.ReviewRepository;
import com.plotori.develop.repository.SubmissionRepository;
import com.plotori.develop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User reviewer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Submission submission = submissionRepository.findById(request.getSubmissionId())
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        // Prevent self-review
        if (submission.getUser().getId().equals(reviewer.getId())) {
            throw new RuntimeException("Cannot review your own submission");
        }

        // Prevent duplicate reviews
        if (reviewRepository.existsBySubmissionIdAndReviewerId(submission.getId(), reviewer.getId())) {
            throw new RuntimeException("You have already reviewed this submission");
        }

        Review review = Review.builder()
                .submission(submission)
                .reviewer(reviewer)
                .characterFidelity(request.getCharacterFidelity())
                .textualIntelligence(request.getTextualIntelligence())
                .creativeOriginality(request.getCreativeOriginality())
                .stylisticCraft(request.getStylisticCraft())
                .interpretiveInsight(request.getInterpretiveInsight())
                .comment(request.getComment())
                .build();

        Review saved = reviewRepository.save(review);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsBySubmission(Long submissionId) {
        return reviewRepository.findBySubmissionId(submissionId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .reviewerUsername(review.getReviewer().getUsername())
                .characterFidelity(review.getCharacterFidelity())
                .textualIntelligence(review.getTextualIntelligence())
                .creativeOriginality(review.getCreativeOriginality())
                .stylisticCraft(review.getStylisticCraft())
                .interpretiveInsight(review.getInterpretiveInsight())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
