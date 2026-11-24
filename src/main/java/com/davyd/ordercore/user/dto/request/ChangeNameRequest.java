package com.davyd.ordercore.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeNameRequest(@NotBlank @Size(max = 100) String name) {
}
