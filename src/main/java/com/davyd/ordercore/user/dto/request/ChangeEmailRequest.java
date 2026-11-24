package com.davyd.ordercore.user.dto.request;

import com.davyd.ordercore.common.validation.ValidUserEmail;

public record ChangeEmailRequest(@ValidUserEmail String email) {
}
