package com.plotori.develop.controller;

import com.plotori.develop.dto.request.ReviewRequest;
import com.plotori.develop.dto.response.ReviewResponse;
import com.plotori.develop.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions/{submissionId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long submissionId,
            @Valid @RequestBody ReviewRequest request) {
        // Ensure the submissionId in path matches request
        request.setSubmissionId(submissionId);
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long submissionId) {
        return ResponseEntity.ok(reviewService.getReviewsBySubmission(submissionId));
    }
}
