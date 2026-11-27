package com.davyd.ordercore.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeleteAccountRequest(@NotBlank @Size(max = 255) String currentPassword) {
}
