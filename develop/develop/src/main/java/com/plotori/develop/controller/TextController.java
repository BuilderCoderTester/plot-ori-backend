package com.plotori.develop.controller;

import com.plotori.develop.dto.response.TextResponse;
import com.plotori.develop.service.TextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/texts")
@RequiredArgsConstructor
@Tag(name = "Texts", description = "Browse canonical literary texts")
public class TextController {
    private final TextService textService;

    @Operation(summary = "List all texts", description = "Returns all canonical and community texts.")
    @GetMapping
    public ResponseEntity<List<TextResponse>> getAllTexts() {
        return ResponseEntity.ok(textService.getAllTexts());
    }

    @Operation(summary = "Get text by ID", description = "Returns full text details including original passage.")
    @GetMapping("/{id}")
    public ResponseEntity<TextResponse> getTextById(@PathVariable Long id) {
        return ResponseEntity.ok(textService.getTextById(id));
    }
}
