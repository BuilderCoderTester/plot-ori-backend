package com.plotori.develop.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User with longest participation streak")
public class StreakLeaderResponse {

    @Schema(description = "User ID", example = "5")
    private Long userId;

    @Schema(description = "Username", example = "charlie_dedicated")
    private String username;

    @Schema(description = "Consecutive weeks", example = "12")
    private Integer weeklyStreak;

    @Schema(description = "Total submissions", example = "15")
    private Integer submissionsCount;

}
