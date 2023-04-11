package com.laby.projektkrypto.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[\\d]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^A-Za-z0-9]");
    private static final Pattern LENGTH_PATTERN = Pattern.compile(".{8,}");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return LENGTH_PATTERN.matcher(s).find()
                && UPPERCASE_PATTERN.matcher(s).find()
                && LOWERCASE_PATTERN.matcher(s).find()
                && DIGIT_PATTERN.matcher(s).find()
                && SPECIAL_CHAR_PATTERN.matcher(s).find();
    }
}
