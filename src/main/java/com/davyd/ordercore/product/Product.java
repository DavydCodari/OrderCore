package com.davyd.ordercore.product;

import jakarta.persistence.*;
import com.davyd.ordercore.common.util.Validation;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
public class Product {
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = MAX_NAME_LENGTH, nullable = false)
    private String name;

    @Column(length = MAX_DESCRIPTION_LENGTH)
    private String description;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Used for optimistic locking to detect concurrent modifications
    @Version
    @Column(nullable = false)
    private Long version;

    protected Product(){

    }

    public Product(String name, String description, BigDecimal price, int stockQuantity){
        Validation.validateNotBlankAndNotExceedingLength(name, MAX_NAME_LENGTH, "Name of the product");
        Validation.validateNotExceedingLength(description, MAX_DESCRIPTION_LENGTH, "Description of the product");
        Validation.validateBigDecimal(price, "Price");
        if (stockQuantity < 0){
            throw new IllegalArgumentException("Stock quantity must be nonnegative");
        }

        this.name = name;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }
}
