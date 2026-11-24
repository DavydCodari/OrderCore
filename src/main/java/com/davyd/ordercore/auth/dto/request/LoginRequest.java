package com.davyd.ordercore.auth.dto.request;

import com.davyd.ordercore.common.validation.ValidUserEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @ValidUserEmail String email,
        @NotBlank String password) {
}
