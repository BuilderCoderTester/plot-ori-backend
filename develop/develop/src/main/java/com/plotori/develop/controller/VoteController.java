package com.plotori.develop.controller;

import com.plotori.develop.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/submissions/{submissionId}/vote")
@RequiredArgsConstructor
@Tag(name = "Voting", description = "Upvote community submissions")
@SecurityRequirement(name = "bearerAuth")
public class VoteController {
    private final VoteService voteService;

    @Operation(summary = "Upvote a submission", description = "One vote per user per submission.")
    @PostMapping
    public ResponseEntity<Map<String, String>> vote(@PathVariable Long submissionId) {
        voteService.vote(submissionId);
        return ResponseEntity.ok(Map.of("message", "Vote recorded successfully"));
    }

    @Operation(summary = "Get vote count")
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getVoteCount(@PathVariable Long submissionId) {
        long count = voteService.getVoteCount(submissionId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
