package com.davyd.ordercore.product;

import com.davyd.ordercore.product.dto.response.ProductResponse;
import com.davyd.ordercore.product.exception.ProductNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        Product product = productRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return productMapper.toResponse(product);
    }

    public Page<ProductResponse> getProducts(Pageable pageable) {
        return productRepository.findByVisibleTrue(productPage(pageable)).map(productMapper::toResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Page<ProductResponse> getProductsForAdmin(Pageable pageable) {
        return productRepository.findAll(productPage(pageable)).map(productMapper::toResponse);
    }

    private Pageable productPage(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse getProductForAdmin(long id) {
        return productMapper.toResponse(productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id)));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse createProduct(String name, String description, BigDecimal price, int stockQuantity) {
        Product product = new Product(name, description, price, stockQuantity);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProduct(long id, String name, String description, BigDecimal price, Integer stockQuantity, Boolean visible) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.update(name, description, price, stockQuantity, visible);
        productRepository.flush();
        return productMapper.toResponse(product);
    }
}
