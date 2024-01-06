package org.moonshot.server.global.common.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TargetNumberValidator implements ConstraintValidator<ValidTargetNumber, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            return value > 0 && value % 1000 == 0;
        }
    }

}
