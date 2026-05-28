package com.plotori.develop.service;

import com.plotori.develop.domain.entity.Text;
import com.plotori.develop.dto.response.TextResponse;
import com.plotori.develop.repository.TextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TextService {
    private final TextRepository textRepository;

    @Transactional(readOnly = true)
    public List<TextResponse> getAllTexts() {
        return textRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TextResponse> getCanonicalTexts() {
        return textRepository.findByCanonicalStatus("CANONICAL").stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TextResponse getTextById(Long id) {
        Text text = textRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Text not found with id: " + id));
        return mapToResponse(text);
    }

    private TextResponse mapToResponse(Text text) {
        return TextResponse.builder()
                .id(text.getId())
                .title(text.getTitle())
                .author(text.getAuthor())
                .originalPassage(text.getOriginalPassage())
                .publicationYear(text.getPublicationYear())
                .genre(text.getGenre())
                .canonicalStatus(text.getCanonicalStatus())
                .build();
    }
}
