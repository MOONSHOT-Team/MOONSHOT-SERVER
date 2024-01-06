package org.moonshot.server.global.common.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TargetNumberValidator implements ConstraintValidator<ValidTargetNumber, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return (value == null) || (value != null && value > 0 && value % 1000 == 0);
    }
}
