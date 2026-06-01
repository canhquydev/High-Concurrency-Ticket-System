package com.quy.highconcurrency_ticket_system.anotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidate.class)
public @interface EnumValid {

    Class<? extends Enum<?>> enumClass();
    String message() default "Invalid data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
