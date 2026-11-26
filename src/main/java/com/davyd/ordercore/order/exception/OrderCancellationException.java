package com.davyd.ordercore.order.exception;

public class OrderCancellationException extends RuntimeException {
    public OrderCancellationException() {
        super("Only created orders can be cancelled");
    }
}
