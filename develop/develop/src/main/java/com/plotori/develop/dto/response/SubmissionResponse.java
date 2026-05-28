package com.plotori.develop.dto.response;

import com.plotori.develop.domain.enums.SubmissionStatus;
import com.plotori.develop.domain.enums.SubmissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class SubmissionResponse {
    private Long id;
    private String authorUsername;
    private Long textId;
    private String textTitle;
    private SubmissionType type;
    private String title;
    private String content;
    private SubmissionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer voteCount;
}
