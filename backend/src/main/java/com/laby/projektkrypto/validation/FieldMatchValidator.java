package com.laby.projektkrypto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.PropertyAccessorFactory;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstField();
        secondFieldName = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object value1 = PropertyAccessorFactory.forBeanPropertyAccess(o).getPropertyValue(firstFieldName);
        Object value2 = PropertyAccessorFactory.forBeanPropertyAccess(o).getPropertyValue(secondFieldName);

        return (value1 != null && value1.equals(value2));
    }
}
