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
@Schema(description = "Top community reviewer")
public class TopReviewerResponse {
    @Schema(description = "User ID", example = "3")
    private Long userId;

    @Schema(description = "Username", example = "bob_reviewer")
    private String username;

    @Schema(description = "Total reviews given", example = "47")
    private Integer reviewsGiven;

    @Schema(description = "Reviews marked helpful", example = "32")
    private Integer reviewsReceivedHelpful;
}
