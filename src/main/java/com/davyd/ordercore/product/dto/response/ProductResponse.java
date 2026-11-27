package com.davyd.ordercore.product.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        boolean visible,
        Instant createdAt
) {
}
