package com.codifica.chatbot.core.application.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ValidationUtil {

    private static final int DEFAULT_MAX_LENGTH = 100;
    private static final Pattern INVALID_CHAR_PATTERN = Pattern.compile("[^\\p{L}0-9\\s.,_\\-@!?#$%&()\\[\\]{}]");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");

    public static String validate(String input, int maxLength) {
        if (input == null || input.trim().isEmpty()) {
            return "Parece que você enviou uma mensagem vazia. Por favor, tente novamente para continuarmos.";
        }

        if (input.length() > maxLength) {
            return "A mensagem que você enviou é muito longa. Por favor, tente novamente com no máximo " + maxLength + " caracteres.";
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

    public static String validate(String input) {
        return validate(input, DEFAULT_MAX_LENGTH);
    }

    public static String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "Parece que você enviou uma mensagem vazia. Por favor, tente novamente para continuarmos.";
        }
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return "O número de telefone parece inválido. Por favor, digite apenas os 11 dígitos do seu celular, incluindo o DDD, sem espaços, traços, parênteses ou código de país.";
        }
        return null;
    }

    public static String validateCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            return "Parece que você enviou uma mensagem vazia. Por favor, tente novamente para continuarmos.";
        }
        if (!CEP_PATTERN.matcher(cep).matches()) {
            return "O CEP parece inválido. Por favor, digite apenas os 8 números do seu CEP.";
        }
        return null;
    }
}
