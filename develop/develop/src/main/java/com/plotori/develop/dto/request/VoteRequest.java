package com.plotori.develop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Vote for next week's text")
public class VoteRequest {
    @Schema(description = "Text ID to vote for", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long textId;
}
