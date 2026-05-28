package com.plotori.develop.dto.response;

import com.plotori.develop.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT authentication response")
public class AuthResponse {
    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIs...")
    private String token;
    @Schema(description = "Token type", example = "Bearer")
    private String type = "Bearer";
    @Schema(description = "User ID", example = "1")
    private Long userId;
    @Schema(description = "Username", example = "alice_writer")
    private String username;
    @Schema(description = "User role", example = "READER")
    private Role role;
}
