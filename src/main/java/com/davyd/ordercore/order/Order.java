package com.davyd.ordercore.order;

import jakarta.persistence.*;
import com.davyd.ordercore.common.util.Validation;
import com.davyd.ordercore.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private OrderStatus status;

    protected Order() {
    }

    public Order(User customer, OrderStatus status) {
        Validation.validateNotNull(customer, "Customer");
        Validation.validateNotNull(status, "Order status");
        this.customer = customer;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
