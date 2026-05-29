package com.plotori.develop.controller;

import com.plotori.develop.dto.response.*;
import com.plotori.develop.service.LeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
@Tag(name = "Leaderboard", description = "Community recognition, streaks, and genre mastery")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @Operation(
            summary = "Weekly winner spotlight",
            description = "Featured submission from the most recent completed week."
    )
    @GetMapping("/spotlight")
    public ResponseEntity<WinnerSpotlightResponse> getSpotlight() {
        WinnerSpotlightResponse spotlight = leaderboardService.getWeeklyWinnerSpotlight();
        return spotlight != null
                ? ResponseEntity.ok(spotlight)
                : ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Top reviewers",
            description = "Users who gave the most structured peer reviews."
    )
    @GetMapping("/top-reviewers")
    public ResponseEntity<List<TopReviewerResponse>> getTopReviewers() {
        return ResponseEntity.ok(leaderboardService.getTopReviewers());
    }

    @Operation(
            summary = "Streak leaders",
            description = "Users with longest consecutive weeks of participation."
    )
    @GetMapping("/streaks")
    public ResponseEntity<List<StreakLeaderResponse>> getStreakLeaders() {
        return ResponseEntity.ok(leaderboardService.getStreakLeaders());
    }

    @Operation(
            summary = "Genre masters",
            description = "Most submissions per canonical author (Shakespeare, Brontë, etc.)."
    )
    @GetMapping("/genre-masters")
    public ResponseEntity<List<GenreMasterResponse>> getGenreMasters() {
        return ResponseEntity.ok(leaderboardService.getGenreMasters());
    }

    @Operation(
            summary = "User stats",
            description = "Complete stats for a specific user."
    )
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserStatsResponse> getUserStats(@PathVariable Long userId) {
        return ResponseEntity.ok(leaderboardService.getUserStats(userId));
    }
}
