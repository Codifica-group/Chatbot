package com.codifica.chatbot.core.application.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ValidationUtil {

    private static final int MAX_LENGTH = 100;
    private static final Pattern INVALID_CHAR_PATTERN = Pattern.compile("[^\\p{L}0-9\\s.,_\\-@!?#$%&()\\[\\]{}]");

    public static String validate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "Parece que você enviou uma mensagem vazia. Por favor, tente novamente para continuarmos.";
        }

        if (input.length() > MAX_LENGTH) {
            return "A mensagem que você enviou é muito longa. Por favor, tente novamente com no máximo " + MAX_LENGTH + " caracteres.";
        }

        Matcher invalidMatcher = INVALID_CHAR_PATTERN.matcher(input);
        Set<String> invalidCharsSet = new HashSet<>();
        while (invalidMatcher.find()) {
            invalidCharsSet.add(invalidMatcher.group());
        }

        if (!invalidCharsSet.isEmpty()) {
            String invalidChars = invalidCharsSet.stream()
                    .sorted()
                    .collect(Collectors.joining("', '", "'", "'"));
            return String.format("Parece que você usou caracteres proibidos. Por favor, tente novamente sem usar: %s.", invalidChars);
        }

        return null;
    }
}
