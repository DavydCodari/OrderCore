package com.davyd.ordercore.product;

import com.davyd.ordercore.product.dto.response.ProductResponse;
import com.davyd.ordercore.product.exception.ProductNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper){
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse getProduct(long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return productMapper.toResponse(product);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse createProduct(String name, String description, BigDecimal price, int stockQuantity) {
        Product product = new Product(name, description, price, stockQuantity);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProduct(long id, String name, String description, BigDecimal price, Integer stockQuantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.update(name, description, price, stockQuantity);
        productRepository.flush();
        return productMapper.toResponse(product);
    }
}
