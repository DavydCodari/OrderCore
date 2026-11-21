package org.example.ordercore.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Size(min = 1, max = 100) @NotBlank String name,
        @Size(min = 3, max = 255) @Email @NotBlank String email,
        @Size(min = 17, max = 255) @NotBlank String password
) {
}
