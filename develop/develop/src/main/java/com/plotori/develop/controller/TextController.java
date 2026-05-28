package com.plotori.develop.controller;

import com.plotori.develop.dto.response.TextResponse;
import com.plotori.develop.service.TextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/texts")
@RequiredArgsConstructor
public class TextController {
    private final TextService textService;

    @GetMapping
    public ResponseEntity<List<TextResponse>> getAllTexts() {
        return ResponseEntity.ok(textService.getAllTexts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TextResponse> getTextById(@PathVariable Long id) {
        return ResponseEntity.ok(textService.getTextById(id));
    }
}
