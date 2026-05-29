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
@Schema(description = "Most submissions per author genre")
public class GenreMasterResponse {

    @Schema(description = "User ID", example = "2")
    private Long userId;

    @Schema(description = "Username", example = "shakespeare_fan")
    private String username;

    @Schema(description = "Shakespeare submissions", example = "8")
    private Integer shakespeareCount;

    @Schema(description = "Brontë submissions", example = "3")
    private Integer bronteCount;

    @Schema(description = "Total mastery score", example = "11")
    private Integer totalMastery;
}
