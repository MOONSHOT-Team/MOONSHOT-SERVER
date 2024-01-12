package org.moonshot.server.global.common.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TargetNumberValidator implements ConstraintValidator<ValidTargetNumber, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            return value > 0 && value % 1000 == 0;
        }
    }

}
