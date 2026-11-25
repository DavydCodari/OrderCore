package com.davyd.ordercore.cart.exception;

import com.davyd.ordercore.common.exception.NotFoundException;

public class ShoppingCartNotFoundException extends NotFoundException {
    public ShoppingCartNotFoundException(long id) {
        super("Shopping cart for user under ID " + id + " not found");
    }
}
