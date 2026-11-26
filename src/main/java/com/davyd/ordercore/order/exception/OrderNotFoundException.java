package com.davyd.ordercore.order.exception;

import com.davyd.ordercore.common.exception.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException(long orderId) {
        super("Order under ID " + orderId + " not found");
    }
}
