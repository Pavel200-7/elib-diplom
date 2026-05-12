package com.example.elib.common.validation.anotations.rule;

import com.example.elib.common.validation.anotations.CommonLetters;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CommonLettersValidator implements ConstraintValidator<CommonLetters, String> {

    private static final Pattern LETTERS_ONLY = Pattern.compile("^[A-Za-zА-Яа-яЁё]+$");
    private static final Pattern LETTERS_WITH_HYPHEN_APOSTROPHE = Pattern.compile("^[A-Za-zА-Яа-яЁё'\\-]+$");

    private boolean nullable;
    private Pattern pattern;

    @Override
    public void initialize(CommonLetters constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
        boolean allowHyphen = constraintAnnotation.allowHyphenAndApostrophe();
        this.pattern = allowHyphen ? LETTERS_WITH_HYPHEN_APOSTROPHE : LETTERS_ONLY;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (nullable && (value == null || value.isBlank())) {
            return true;
        }
        if (value == null) {
            return false;
        }
        String trimmed = value.trim();
        if (nullable && trimmed.isEmpty()) {
            return true;
        }
        return pattern.matcher(trimmed).matches();
    }
}