package com.plotori.develop.controller;

import com.plotori.develop.dto.response.WeeklyPromptResponse;
import com.plotori.develop.service.WeeklyPromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weekly-prompt")
@RequiredArgsConstructor
public class WeeklyPromptController {
    private final WeeklyPromptService weeklyPromptService;

    @GetMapping
    public ResponseEntity<WeeklyPromptResponse> getCurrentPrompt() {
        return ResponseEntity.ok(weeklyPromptService.getCurrentWeeklyPrompt());
    }
}
