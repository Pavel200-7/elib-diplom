package com.example.elib.common.validation.anotations;

import com.example.elib.common.validation.anotations.rule.CommonLettersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CommonLettersValidator.class)
@Documented
public @interface CommonLetters {
    String message() default "Поле может содержать только буквы (русские или английские)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean nullable() default false;
    boolean allowHyphenAndApostrophe() default false;
}