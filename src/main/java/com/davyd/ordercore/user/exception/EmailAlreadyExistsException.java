package com.davyd.ordercore.user.exception;

import com.davyd.ordercore.common.exception.AlreadyExistsException;

public class EmailAlreadyExistsException extends AlreadyExistsException {
    public EmailAlreadyExistsException(String email){
        super("User with email " + email + " already exists");
    }
}
