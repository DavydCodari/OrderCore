package com.davyd.ordercore.order.exception;

public class OrderDeliveryException extends RuntimeException {
    public OrderDeliveryException() {
        super("Only processing orders can be marked as delivered");
    }
}
