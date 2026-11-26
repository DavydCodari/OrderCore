package com.davyd.ordercore.order.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderItemResponse(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal price,
        Instant createdAt
) {
}
