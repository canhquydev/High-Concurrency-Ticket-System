package com.quy.highconcurrency_ticket_system.anotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class EnumValidate implements ConstraintValidator<EnumValid, String> {
    List<String> enums;

    @Override
    public void initialize(EnumValid annotation) {
        enums = new ArrayList<>();
        Enum<?>[] values = annotation.enumClass().getEnumConstants();
        for(Enum<?> e : values){
            enums.add(e.name());
        }
        ConstraintValidator.super.initialize(annotation);
    }

    @Override
    public boolean isValid(String anEnum, ConstraintValidatorContext constraintValidatorContext) {
        if(anEnum == null || anEnum.isBlank())
        {
            return true;
        }
        return enums.contains(anEnum.toUpperCase());
    }
}
