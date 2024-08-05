package com.prography.yakgwa.domain.common.validator.datetime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatValidator implements ConstraintValidator<DateTimeValid, String> {
    private String pattern;

    @Override
    public void initialize(DateTimeValid constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDateTime parse = LocalDateTime.parse(value, DateTimeFormatter.ofPattern(this.pattern));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
