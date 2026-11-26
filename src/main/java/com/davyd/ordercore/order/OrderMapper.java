package com.davyd.ordercore.order;

import com.davyd.ordercore.order.dto.response.OrderItemResponse;
import com.davyd.ordercore.order.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public OrderResponse toResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(item -> new OrderItemResponse(item.getId(), item.getProduct().getId(),
                        item.getQuantity(), item.getPrice(), item.getCreatedAt()))
                .toList();
        return new OrderResponse(order.getId(), order.getCreatedAt(), order.getStatus(), itemResponses);
    }
}
