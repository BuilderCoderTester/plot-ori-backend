package com.plotori.develop.controller;

import com.plotori.develop.dto.request.SubmissionRequest;
import com.plotori.develop.dto.response.SubmissionResponse;
import com.plotori.develop.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<SubmissionResponse> createSubmission(@Valid @RequestBody SubmissionRequest request) {
        return ResponseEntity.ok(submissionService.createSubmission(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponse> getSubmission(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }

    @GetMapping("/text/{textId}")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByText(@PathVariable Long textId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByText(textId));
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<SubmissionResponse>> getWeeklySubmissions() {
        return ResponseEntity.ok(submissionService.getWeeklySubmissions());
    }

    @GetMapping("/my")
    public ResponseEntity<List<SubmissionResponse>> getMySubmissions() {
        return ResponseEntity.ok(submissionService.getMySubmissions());
    }
}
