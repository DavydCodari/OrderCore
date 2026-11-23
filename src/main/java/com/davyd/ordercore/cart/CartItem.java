package com.davyd.ordercore.cart;

import jakarta.persistence.*;
import com.davyd.ordercore.common.util.Validation;
import com.davyd.ordercore.product.Product;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Integer quantity;

    protected CartItem() {
    }

    public CartItem(Product product, ShoppingCart shoppingCart, int quantity) {
        Validation.validateNotNull(product, "Product");
        Validation.validateNotNull(shoppingCart, "Shopping cart");
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        this.product = product;
        this.shoppingCart = shoppingCart;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
