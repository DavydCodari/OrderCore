package com.davyd.ordercore.order.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super("Cannot create an order from an empty shopping cart");
    }
}
