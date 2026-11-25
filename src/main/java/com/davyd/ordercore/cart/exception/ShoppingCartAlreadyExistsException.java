package com.davyd.ordercore.cart.exception;

import com.davyd.ordercore.common.exception.AlreadyExistsException;

public class ShoppingCartAlreadyExistsException extends AlreadyExistsException {
    public ShoppingCartAlreadyExistsException(long id){
        super("Shopping cart for user under id " + id + " already exists");
    }
}
