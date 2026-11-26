package com.davyd.ordercore.cart.dto.response;

import java.time.Instant;
import java.util.List;

public record ShoppingCartResponse (
        Long shoppingCartId,
        Instant createdAt,
        List<CartItemResponse> items) {
}
