package org.moonshot.server.global.common.model.validator;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TargetNumberValidator.class)
public @interface ValidTargetNumber {

    String message() default "목표치는 1000단위로 입력해야 합니다.";

    Class[] groups() default {};

    Class[] payload() default {};

}
