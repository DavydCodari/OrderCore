package com.davyd.ordercore.user.exception;

import com.davyd.ordercore.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(long userId) {
        super("User under ID " + userId + " not found");
    }

    public UserNotFoundException(String userEmail) {
        super("User with email " + userEmail + " not found");
    }
}
