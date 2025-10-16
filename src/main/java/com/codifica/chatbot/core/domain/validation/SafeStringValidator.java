package com.codifica.chatbot.core.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SafeStringValidator implements ConstraintValidator<SafeString, String> {

    /**
     * WHITELIST Permite:
     * \\p{L}    - Letras
     * 0-9       - Números
     * \\s       - Espaços em branco
     * .,_\\-@!?#$%&()[\\]{} - Caracteres especiais selecionados
     */
    private static final String SAFE_STRING_PATTERN = "^[\\p{L}0-9\\s.,_\\-@!?#$%&()\\[\\]{}]*$";
    private static final Pattern pattern = Pattern.compile(SAFE_STRING_PATTERN);

    private static final Pattern INVALID_CHAR_PATTERN = Pattern.compile("[^\\p{L}0-9\\s.,_\\-@!?#$%&()\\[\\]{}]");

    @Override
    public void initialize(SafeString constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (pattern.matcher(value).matches()) {
            return true;
        }

        Matcher invalidMatcher = INVALID_CHAR_PATTERN.matcher(value);
        String invalidChars = invalidMatcher.results()
                .map(matchResult -> matchResult.group())
                .distinct()
                .collect(Collectors.joining("', '", "'", "'"));

        String message = String.format("A entrada contém caracteres proibidos: %s", invalidChars);

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();

        return false;
    }
}
