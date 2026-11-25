package com.davyd.ordercore.cart;

import com.davyd.ordercore.cart.dto.response.ShoppingCartResponse;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartMapper {
    public ShoppingCartResponse toResponse(ShoppingCart shoppingCart) {
        return new ShoppingCartResponse(shoppingCart.getId(), shoppingCart.getCreatedAt());
    }
}
