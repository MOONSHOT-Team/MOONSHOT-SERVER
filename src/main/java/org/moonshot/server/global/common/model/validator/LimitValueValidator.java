package org.moonshot.server.global.common.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LimitValueValidator implements ConstraintValidator<ValidTargetNumber, Long> {

    private final long MAX_LIMIT = 99999999999L;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            return value <= MAX_LIMIT;
        }
    }

}