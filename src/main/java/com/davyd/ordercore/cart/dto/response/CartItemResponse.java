package com.davyd.ordercore.cart.dto.response;

import java.time.Instant;

public record CartItemResponse(Long id, Long productId, Integer quantity, Instant createdAt) {
}
