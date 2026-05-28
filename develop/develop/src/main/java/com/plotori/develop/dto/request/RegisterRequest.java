package com.plotori.develop.dto.request;

import com.plotori.develop.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "User registration payload")
public class RegisterRequest {
    @Schema(description = "Unique username", example = "alice_writer", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Schema(description = "Valid email address", example = "alice@plotori.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Password (min 6 chars)", example = "securePass123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @Schema(description = "User role", example = "READER", allowableValues = {"READER", "STUDENT", "TEACHER", "ADMIN"})
    @NotNull
    private Role role;
}
