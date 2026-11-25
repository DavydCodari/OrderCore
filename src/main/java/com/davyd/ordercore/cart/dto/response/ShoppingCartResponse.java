package com.davyd.ordercore.cart.dto.response;

import java.time.Instant;

public record ShoppingCartResponse (
        Long shoppingCartId,
        Instant createdAt) {
}