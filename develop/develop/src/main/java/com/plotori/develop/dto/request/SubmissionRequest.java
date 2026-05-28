package com.plotori.develop.dto.request;

import com.plotori.develop.domain.enums.SubmissionStatus;
import com.plotori.develop.domain.enums.SubmissionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmissionRequest {
    @NotNull
    private Long textId;

    @NotNull
    private SubmissionType type;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private SubmissionStatus status = SubmissionStatus.DRAFT;
}
