package org.example.ordercore.cart;

import jakarta.persistence.*;
import org.example.ordercore.common.util.Validation;
import org.example.ordercore.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @Column(name = "shopping_cart_id", nullable = false)
    private Long shoppingCartId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private User customer;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ShoppingCart() {
    }

    public ShoppingCart(User customer) {
        Validation.validateNotNull(customer, "Customer");
        this.customer = customer;
    }

    public Long getId() {
        return shoppingCartId;
    }

    public User getCustomer() {
        return customer;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
