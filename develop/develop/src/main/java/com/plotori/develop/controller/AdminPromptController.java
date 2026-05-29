package com.plotori.develop.controller;


import com.plotori.develop.dto.request.FeatureRequest;
import com.plotori.develop.service.WeeklyPromptService;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/admin/prompts")
@RequiredArgsConstructor
@Tag(name = "Admin Prompt Management", description = "Curate prompts and feature submissions")
@SecurityRequirement(name = "bearerAuth")
public class AdminPromptController {

    private final WeeklyPromptService weeklyPromptService;

    @Operation(summary = "Manually feature a submission", description = "Admin selects winner with editorial note.")
    @PostMapping("/feature")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Map<String, String>> featureSubmission(@Valid @RequestBody FeatureRequest request) {
        weeklyPromptService.featureSubmission(request.getSubmissionId(), request.getEditorialNote());
        return ResponseEntity.ok(Map.of("message", "Submission featured successfully"));
    }

    @Operation(summary = "Force start voting phase", description = "Manual trigger (normally auto-scheduled).")
    @PostMapping("/start-voting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> forceStartVoting() {
        weeklyPromptService.startVotingPhase();
        return ResponseEntity.ok(Map.of("message", "Voting phase started"));
    }

    @Operation(summary = "Force activate prompt", description = "Manual trigger (normally auto-scheduled Monday).")
    @PostMapping("/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> forceActivate() {
        weeklyPromptService.activatePrompt();
        return ResponseEntity.ok(Map.of("message", "Prompt activated"));
    }
}
