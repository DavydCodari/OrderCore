package com.davyd.ordercore.common.validation;

import com.davyd.ordercore.common.util.Validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserEmailValidator implements ConstraintValidator<ValidUserEmail, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Validation.isValidEmail(value);
    }
}
