package com.davyd.ordercore.order;

import com.davyd.ordercore.cart.CartItem;
import com.davyd.ordercore.cart.CartItemRepository;
import com.davyd.ordercore.cart.ShoppingCart;
import com.davyd.ordercore.cart.ShoppingCartRepository;
import com.davyd.ordercore.cart.exception.ShoppingCartNotFoundException;
import com.davyd.ordercore.order.exception.EmptyCartException;
import com.davyd.ordercore.order.exception.InsufficientStockException;
import com.davyd.ordercore.product.Product;
import com.davyd.ordercore.user.User;
import com.davyd.ordercore.user.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderCheckoutTest {
    private final OrderRepository orders = mock(OrderRepository.class);
    private final OrderItemRepository orderItems = mock(OrderItemRepository.class);
    private final ShoppingCartRepository carts = mock(ShoppingCartRepository.class);
    private final CartItemRepository cartItems = mock(CartItemRepository.class);
    private final EntityManager entityManager = mock(EntityManager.class);
    private final OrderService service = new OrderService(orders, orderItems, new OrderMapper(), carts, cartItems, entityManager);

    @Test void createsOrderWithCartQuantitiesAndCurrentPricesThenClearsCart() {
        var customer = new User("Alice", "alice@example.com", UserRole.CUSTOMER);
        var cart = new ShoppingCart(customer);
        var product = new Product("Book", null, new BigDecimal("12.50"), 3);
        when(carts.findForUpdate(42)).thenReturn(Optional.of(cart));
        when(cartItems.findByShoppingCart_ShoppingCartIdOrderByIdAsc(42))
                .thenReturn(List.of(new CartItem(product, cart, 3)));
        when(orders.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.createOrder(42);

        assertEquals(OrderStatus.CREATED, response.status());
        assertEquals(1, response.items().size());
        assertEquals(3, response.items().getFirst().quantity());
        assertEquals(new BigDecimal("12.50"), response.items().getFirst().price());
        var savedOrder = ArgumentCaptor.forClass(Order.class);
        verify(orders).save(savedOrder.capture());
        assertSame(customer, savedOrder.getValue().getCustomer());
        verify(entityManager).lock(product, LockModeType.OPTIMISTIC);
        var sequence = inOrder(orderItems, cartItems);
        sequence.verify(orderItems).saveAll(anyList());
        sequence.verify(orderItems).flush();
        sequence.verify(cartItems).deleteItemsFromCart(42);
    }

    @Test void emptyCartDoesNotCreateOrderOrClearCart() {
        when(carts.findForUpdate(42)).thenReturn(Optional.of(mock(ShoppingCart.class)));
        when(cartItems.findByShoppingCart_ShoppingCartIdOrderByIdAsc(42)).thenReturn(List.of());
        assertThrows(EmptyCartException.class, () -> service.createOrder(42));
        verify(orders, never()).save(any());
        verify(cartItems, never()).deleteItemsFromCart(anyLong());
    }

    @Test void missingCartDoesNotCreateOrder() {
        assertThrows(ShoppingCartNotFoundException.class, () -> service.createOrder(42));
        verify(orders, never()).save(any());
    }

    @Test void insufficientStockRejectsCheckoutWithoutClearingCart() {
        var cart = new ShoppingCart(new User("Alice", "alice@example.com", UserRole.CUSTOMER));
        var product = mock(Product.class);
        when(product.getId()).thenReturn(7L);
        when(product.getStockQuantity()).thenReturn(2);
        when(carts.findForUpdate(42)).thenReturn(Optional.of(cart));
        when(cartItems.findByShoppingCart_ShoppingCartIdOrderByIdAsc(42))
                .thenReturn(List.of(new CartItem(product, cart, 3)));
        when(orders.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var error = assertThrows(InsufficientStockException.class, () -> service.createOrder(42));
        assertEquals("Insufficient stock for product 7", error.getMessage());
        verify(orderItems, never()).saveAll(anyList());
        verify(cartItems, never()).deleteItemsFromCart(anyLong());
    }
}
