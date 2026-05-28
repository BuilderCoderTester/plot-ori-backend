package com.plotori.develop.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyPromptResponse {
    private Long textId;
    private String textTitle;
    private String textAuthor;
    private String promptDescription;
    private LocalDate weekStart;
    private LocalDate weekEnd;
}
