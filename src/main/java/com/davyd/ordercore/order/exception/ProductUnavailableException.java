package com.davyd.ordercore.order.exception;

public class ProductUnavailableException extends RuntimeException {
    public ProductUnavailableException(long productId) {
        super("Product " + productId + " is no longer available; remove it from your cart");
    }
}
