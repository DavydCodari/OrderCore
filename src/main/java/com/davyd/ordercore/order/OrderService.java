package com.davyd.ordercore.order;

import com.davyd.ordercore.order.dto.response.OrderResponse;
import com.davyd.ordercore.order.exception.OrderNotFoundException;
import com.davyd.ordercore.order.exception.OrderCancellationException;
import com.davyd.ordercore.order.exception.OrderDeliveryException;
import com.davyd.ordercore.order.exception.EmptyCartException;
import com.davyd.ordercore.order.exception.InsufficientStockException;
import com.davyd.ordercore.order.exception.ProductUnavailableException;
import com.davyd.ordercore.cart.CartItemRepository;
import com.davyd.ordercore.cart.ShoppingCartRepository;
import com.davyd.ordercore.cart.exception.ShoppingCartNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final EntityManager entityManager;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        OrderMapper orderMapper, ShoppingCartRepository shoppingCartRepository,
                        CartItemRepository cartItemRepository, EntityManager entityManager) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemRepository = cartItemRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public OrderResponse createOrder(long customerId) {
        var cart = shoppingCartRepository.findForUpdate(customerId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(customerId));
        var cartItems = cartItemRepository.findByShoppingCart_ShoppingCartIdOrderByIdAsc(customerId);
        if (cartItems.isEmpty()) {
            throw new EmptyCartException();
        }
        var order = orderRepository.save(new Order(cart.getCustomer(), OrderStatus.CREATED));
        List<OrderItem> items = cartItems.stream().map(item -> {
            var product = item.getProduct();
            entityManager.lock(product, LockModeType.OPTIMISTIC);
            if (!product.isVisible()) {
                throw new ProductUnavailableException(product.getId());
            }
            if (item.getQuantity() > product.getStockQuantity()) {
                throw new InsufficientStockException(product.getId());
            }
            return new OrderItem(product, order, item.getQuantity(), product.getPrice());
        }).toList();
        orderItemRepository.saveAll(items);
        orderItemRepository.flush();
        cartItemRepository.deleteItemsFromCart(customerId);
        return orderMapper.toResponse(order, items);
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
