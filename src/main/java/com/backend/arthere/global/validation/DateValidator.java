package com.backend.arthere.global.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateValidator implements ConstraintValidator<Date, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        Pattern pattern = Pattern.compile("yyyy-MM-dd'T'HH:mm:ss.nnnnnn");
        Matcher matcher = pattern.matcher(value);

        if (matcher.matches()) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime request = LocalDateTime.parse(value);
            return request.isBefore(now);
        }
        return false;
    }
}
