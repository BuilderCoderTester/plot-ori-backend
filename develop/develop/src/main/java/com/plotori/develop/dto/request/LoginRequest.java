package com.plotori.develop.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "User login credentials")
public class LoginRequest {
    @Schema(description = "Username", example = "alice_writer", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String username;

    @Schema(description = "Password", example = "securePass123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String password;
}
