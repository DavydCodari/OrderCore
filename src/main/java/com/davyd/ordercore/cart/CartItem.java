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

    @Column(nullable = false)
    private Integer quantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Used for optimistic locking to detect concurrent modifications
    @Version
    @Column(nullable = false)
    private Long version;

    protected CartItem() {
    }

    public CartItem(Product product, ShoppingCart shoppingCart, int quantity) {
        Validation.validateNotNull(product, "Product");
        Validation.validateNotNull(shoppingCart, "Shopping cart");
        this.product = product;
        this.shoppingCart = shoppingCart;
        setQuantity(quantity);
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
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
