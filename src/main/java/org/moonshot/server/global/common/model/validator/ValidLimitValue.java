package org.moonshot.server.global.common.model.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TargetNumberValidator.class)
public @interface ValidLimitValue {

    String message() default "수치는 99,999,999,999까지만 가능합니다.";

    Class[] groups() default {};

    Class[] payload() default {};

}
