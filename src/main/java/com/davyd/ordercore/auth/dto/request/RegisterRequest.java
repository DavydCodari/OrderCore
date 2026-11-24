package com.davyd.ordercore.auth.dto.request;

import com.davyd.ordercore.common.validation.ValidUserEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Size(min = 1, max = 100) @NotBlank String name,
        @ValidUserEmail String email,
        @Size(min = 17, max = 255) @NotBlank String password
) {
}
