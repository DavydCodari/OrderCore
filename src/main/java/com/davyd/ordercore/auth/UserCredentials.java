package com.davyd.ordercore.auth;

import jakarta.persistence.*;
import com.davyd.ordercore.common.util.Validation;
import com.davyd.ordercore.user.User;

@Entity
@Table(name = "user_credentials")
public class UserCredentials {
    private static final int MAX_HASHED_PASSWORD_LENGTH = 255;

    @Id
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User user;

    @Column(name = "hashed_password", length = MAX_HASHED_PASSWORD_LENGTH, nullable = false)
    private String hashedPassword;

    protected UserCredentials() {
    }

    public UserCredentials(User user, String hashedPassword) {
        Validation.validateNotNull(user, "User");
        Validation.validateNotBlankAndNotExceedingLength(
                hashedPassword, MAX_HASHED_PASSWORD_LENGTH, "Hashed password");

        this.user = user;
        this.hashedPassword = hashedPassword;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public User getUser() {
        return user;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
