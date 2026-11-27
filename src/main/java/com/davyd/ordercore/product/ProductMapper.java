package com.davyd.ordercore.product;

import com.davyd.ordercore.product.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponse toResponse(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.isVisible(),
                product.getCreatedAt()
        );
    }
}
