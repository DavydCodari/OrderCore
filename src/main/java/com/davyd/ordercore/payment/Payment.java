package com.davyd.ordercore.payment;

import jakarta.persistence.*;
import com.davyd.ordercore.common.util.Validation;
import com.davyd.ordercore.order.Order;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {
    private static final int MAX_CURRENCY_LENGTH = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(length = MAX_CURRENCY_LENGTH, nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Version
    @Column(nullable = false)
    private Long version;

    protected Payment() {
    }

    public Payment(Order order, BigDecimal price, String currency, PaymentStatus status) {
        Validation.validateNotNull(order, "Order");
        Validation.validateBigDecimal(price, "Price");
        Validation.validateNotBlankAndNotExceedingLength(currency, MAX_CURRENCY_LENGTH, "Currency");
        if (!currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency must contain three uppercase letters");
        }
        Validation.validateNotNull(status, "Status");

        this.order = order;
        this.price = price;
        this.currency = currency;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
