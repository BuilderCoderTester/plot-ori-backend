package com.plotori.develop.controller;

import com.plotori.develop.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/submissions/{submissionId}/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Map<String, String>> vote(@PathVariable Long submissionId) {
        voteService.vote(submissionId);
        return ResponseEntity.ok(Map.of("message", "Vote recorded successfully"));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getVoteCount(@PathVariable Long submissionId) {
        long count = voteService.getVoteCount(submissionId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
