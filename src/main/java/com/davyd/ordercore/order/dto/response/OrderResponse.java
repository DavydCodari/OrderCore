package com.davyd.ordercore.order.dto.response;

import com.davyd.ordercore.order.OrderStatus;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Instant createdAt,
        OrderStatus status,
        List<OrderItemResponse> items
) {
}
