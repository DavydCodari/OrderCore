package com.davyd.ordercore.cart.exception;

import com.davyd.ordercore.common.exception.NotFoundException;

public class CartItemNotFoundException extends NotFoundException {
    public CartItemNotFoundException(long itemId) {
        super("Cart item under ID " + itemId + " not found");
    }
}
