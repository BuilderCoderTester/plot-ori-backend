package com.plotori.develop.controller;

import com.plotori.develop.dto.request.ReviewRequest;
import com.plotori.develop.dto.response.ReviewResponse;
import com.plotori.develop.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions/{submissionId}/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Structured peer reviews with 5-part rubric")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Leave a review",
            description = "Submit a structured review with rubric scores (1-5) and comment. Cannot review own work."
    )
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long submissionId,
            @Valid @RequestBody ReviewRequest request) {
        // Ensure the submissionId in path matches request
        request.setSubmissionId(submissionId);
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @Operation(summary = "Get all reviews for a submission")
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long submissionId) {
        return ResponseEntity.ok(reviewService.getReviewsBySubmission(submissionId));
    }
}
