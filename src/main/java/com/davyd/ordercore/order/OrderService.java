package com.davyd.ordercore.order;

import com.davyd.ordercore.order.dto.response.OrderResponse;
import com.davyd.ordercore.order.exception.OrderNotFoundException;
import com.davyd.ordercore.order.exception.OrderCancellationException;
import com.davyd.ordercore.order.exception.OrderDeliveryException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
    }

    public OrderResponse getOrder(long currentUserId, long orderId) {
        Order order = orderRepository.findByIdAndCustomer_Id(orderId, currentUserId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.toResponse(order, orderItemRepository.findByOrder_IdOrderByIdAsc(orderId));
    }

    @Transactional
    public void cancelOrder(long currentUserId, long orderId) {
        int updated = orderRepository.cancelCreatedOrder(orderId, currentUserId,
                OrderStatus.CREATED, OrderStatus.CANCELLED);
        if (updated == 0) {
            if (!orderRepository.existsByIdAndCustomer_Id(orderId, currentUserId)) {
                throw new OrderNotFoundException(orderId);
            }
            throw new OrderCancellationException();
        }
    }

    @Transactional
    public void confirmDelivery(long currentUserId, long orderId) {
        int updated = orderRepository.confirmDelivery(orderId, currentUserId,
                OrderStatus.PROCESSING, OrderStatus.DELIVERED);
        if (updated == 0) {
            if (!orderRepository.existsByIdAndCustomer_Id(orderId, currentUserId)) {
                throw new OrderNotFoundException(orderId);
            }
            throw new OrderDeliveryException();
        }
    }
}
