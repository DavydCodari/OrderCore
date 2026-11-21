package org.example.ordercore.user.exception;

import org.example.ordercore.common.exception.AlreadyExistsException;

public class EmailAlreadyExistsException extends AlreadyExistsException {
    public EmailAlreadyExistsException(String email){
        super("User with email " + email + " already exists");
    }
}
