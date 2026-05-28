package com.plotori.develop.controller;

import com.plotori.develop.dto.request.SubmissionRequest;
import com.plotori.develop.dto.response.SubmissionResponse;
import com.plotori.develop.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Tag(name = "Submissions", description = "Submit creative writing pieces and browse community work")
@SecurityRequirement(name = "bearerAuth")
public class SubmissionController {
    private final SubmissionService submissionService;

    @Operation(
            summary = "Create a submission",
            description = "Submit a draft or final piece. Set status to DRAFT or SUBMITTED."
    )
    @PostMapping
    public ResponseEntity<SubmissionResponse> createSubmission(@Valid @RequestBody SubmissionRequest request) {
        return ResponseEntity.ok(submissionService.createSubmission(request));
    }

    @Operation(summary = "Get submission by ID")
    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponse> getSubmission(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }

    @Operation(summary = "List submissions for a text")
    @GetMapping("/text/{textId}")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByText(@PathVariable Long textId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByText(textId));
    }

    @Operation(summary = "Weekly feed", description = "All SUBMITTED works for the current week.")
    @GetMapping("/weekly")
    public ResponseEntity<List<SubmissionResponse>> getWeeklySubmissions() {
        return ResponseEntity.ok(submissionService.getWeeklySubmissions());
    }

    @Operation(summary = "My submissions", description = "All drafts and submissions by the authenticated user.")
    @GetMapping("/my")
    public ResponseEntity<List<SubmissionResponse>> getMySubmissions() {
        return ResponseEntity.ok(submissionService.getMySubmissions());
    }
}
