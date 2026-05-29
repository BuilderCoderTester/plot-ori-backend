package com.plotori.develop.controller;

import com.plotori.develop.dto.response.WeeklyPromptResponse;
import com.plotori.develop.service.WeeklyPromptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weekly-prompt")
@RequiredArgsConstructor
@Tag(name = "Weekly Prompt", description = "Current week's active writing prompt")
public class WeeklyPromptController {
    private final WeeklyPromptService weeklyPromptService;

    @Operation(
            summary = "Get current weekly prompt",
            description = "Returns the active text and prompt for this week. Rotates through canonical texts."
    )
    @GetMapping
    public ResponseEntity<WeeklyPromptResponse> getCurrentPrompt() {
        return ResponseEntity.ok(weeklyPromptService.getCurrentWeeklyPrompt());
    }

    @Operation(summary = "Get prompt history", description = "All past prompts with featured submissions.")
    @GetMapping("/history")
    public ResponseEntity<List<WeeklyPromptResponse>> getHistory() {
        return ResponseEntity.ok(weeklyPromptService.getPromptHistory());
    }
}
