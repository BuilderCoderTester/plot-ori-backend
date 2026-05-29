package com.plotori.develop.controller;
import com.plotori.develop.dto.request.VoteRequest;
import com.plotori.develop.service.WeeklyPromptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prompts/voting")
@RequiredArgsConstructor
@Tag(name = "Prompt Voting", description = "Community voting for next week's text")
@SecurityRequirement(name = "bearerAuth")
public class PromptVoteController {

    private final WeeklyPromptService weeklyPromptService;

    @Operation(summary = "Cast vote for next week's text", description = "One vote per user during voting phase.")
    @PostMapping
    public ResponseEntity<Map<String, String>> castVote(@Valid @RequestBody VoteRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // You'll need to inject UserRepository or get userId from token
        // For now, simplified — you'd fetch user by username
        weeklyPromptService.castVote(getCurrentUserId(), request.getTextId());
        return ResponseEntity.ok(Map.of("message", "Vote recorded"));
    }

    @Operation(summary = "Get current voting results", description = "Live tally of votes.")
    @GetMapping("/results")
    public ResponseEntity<List<WeeklyPromptService.VoteResult>> getResults() {
        return ResponseEntity.ok(weeklyPromptService.getVotingResults());
    }

    private Long getCurrentUserId() {
        // Implement: get from UserRepository by username
        return 1L; // Placeholder
    }
}
