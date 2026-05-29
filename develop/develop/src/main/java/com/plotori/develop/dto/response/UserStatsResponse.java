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
@Schema(description = "Complete user profile statistics")
public class UserStatsResponse {
    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "Username", example = "alice_writer")
    private String username;

    @Schema(description = "Role", example = "READER")
    private String role;

    @Schema(description = "Total submissions", example = "23")
    private Integer totalSubmissions;

    @Schema(description = "Reviews given", example = "45")
    private Integer totalReviewsGiven;

    @Schema(description = "Current weekly streak", example = "5")
    private Integer currentStreak;

    @Schema(description = "Times featured", example = "2")
    private Integer featuredSubmissions;

    @Schema(description = "Shakespeare pieces", example = "10")
    private Integer shakespeareMastery;

    @Schema(description = "Brontë pieces", example = "5")
    private Integer bronteMastery;
}
