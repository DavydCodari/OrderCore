package com.davyd.ordercore.cart;

import com.davyd.ordercore.cart.dto.response.ShoppingCartResponse;
import com.davyd.ordercore.cart.dto.response.CartItemResponse;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ShoppingCartMapper {
    public ShoppingCartResponse toResponse(ShoppingCart shoppingCart, List<CartItem> items) {
        return new ShoppingCartResponse(shoppingCart.getId(), shoppingCart.getCreatedAt(),
                items.stream().map(this::toItemResponse).toList());
    }

    public CartItemResponse toItemResponse(CartItem item) {
        return new CartItemResponse(item.getId(), item.getProduct().getId(), item.getQuantity(), item.getCreatedAt());
    }
}
