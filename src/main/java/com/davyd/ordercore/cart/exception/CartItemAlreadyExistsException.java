package com.davyd.ordercore.cart.exception;

import com.davyd.ordercore.common.exception.AlreadyExistsException;

public class CartItemAlreadyExistsException extends AlreadyExistsException {
    public CartItemAlreadyExistsException(long productId) {
        super("Product under ID " + productId + " is already in the cart");
    }
}
