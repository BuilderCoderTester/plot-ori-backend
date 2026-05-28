package com.plotori.develop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextResponse {
    private Long id;
    private String title;
    private String author;
    private String originalPassage;
    private Integer publicationYear;
    private String genre;
    private String canonicalStatus;
}
