package org.example.ordercore.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.example.ordercore.common.util.Validation;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {
    private static final int MAX_NAME_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = Validation.EMAIL_REGEX, message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = MAX_NAME_LENGTH, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Used for optimistic locking to detect concurrent modifications
    @Version
    @Column(nullable = false)
    private Long version;

    protected User() {
    }

    public User(String name, String email, UserRole userRole){
        Validation.validateNotBlankAndNotExceedingLength(name, MAX_NAME_LENGTH, "Name of the user");
        Validation.validateEmail(email);
        Validation.validateNotNull(userRole, "User role");

        this.name = name;
        this.email = email;
        this.role = userRole;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
