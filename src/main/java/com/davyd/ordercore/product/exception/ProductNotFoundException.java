package com.davyd.ordercore.product.exception;

import com.davyd.ordercore.common.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(long productId) {
        super("Product under ID " + productId + " not found");
    }
}
