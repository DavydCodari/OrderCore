package com.davyd.ordercore.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemQuantityRequest(@NotNull @Positive Integer quantity) {
}
