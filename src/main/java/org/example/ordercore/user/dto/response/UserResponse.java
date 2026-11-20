package org.example.ordercore.user.dto.response;

import org.example.ordercore.user.UserRole;

import java.time.Instant;

public record UserResponse (
        Long id,
        String email,
        String name,
        UserRole role,
        Instant created_at
)
{
}