package com.codifica.chatbot.core.application.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
            return "Ops! Parece que sua mensagem foi vazia. Tente novamente para continuarmos, por favor";
        }

        if (input.length() > maxLength) {
            return "A mensagem que você enviou é muito longa. Por favor, tente novamente com no máximo " + maxLength + " caracteres";
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
            return String.format("Parece que você usou caracteres proibidos. Por favor, tente novamente sem usar: %s", invalidChars);
        }

        return null;
    }

    public static String validate(String input) {
        return validate(input, DEFAULT_MAX_LENGTH);
    }

    public static String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "Ops! Parece que sua mensagem foi vazia. Tente novamente para continuarmos, por favor";
        }
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return "O número de telefone parece inválido. Por favor, digite apenas os 11 dígitos do seu celular, incluindo o DDD, sem espaços, traços, parênteses ou código de país";
        }
        return null;
    }

    public static String validateCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            return "Ops! Parece que sua mensagem foi vazia. Tente novamente para continuarmos, por favor";
        }
        if (!CEP_PATTERN.matcher(cep).matches()) {
            return "O CEP parece inválido. Por favor, digite apenas os 8 números do seu CEP";
        }
        return null;
    }

    public static String validateIntegerChoice(String input, int maxChoice) {
        if (input == null || input.trim().isEmpty()) {
            return "Ops! Parece que sua mensagem foi vazia. Tente novamente para continuarmos, por favor";
        }

        try {
            int choice = Integer.parseInt(input.trim());
            if (choice < 1 || choice > maxChoice) {
                return "Essa não é uma opção válida. Por favor, escolha uma das opções acima";
            }
        } catch (NumberFormatException e) {
            return "Por favor, digite apenas o número correspondente à opção desejada";
        }

        return null;
    }

    public static String validateServiceChoice(String input, int maxChoice) {
        if (input == null || input.trim().isEmpty()) {
            return "Ops! Parece que sua mensagem foi vazia. Tente novamente para continuarmos, por favor";
        }

        try {
            List<Integer> choices = Arrays.stream(input.split(","))
                    .map(s -> Integer.parseInt(s.trim()))
                    .collect(Collectors.toList());

            for (Integer choice : choices) {
                if (choice < 1 || choice > maxChoice) {
                    return "Opa! Um dos números que você digitou (" + choice + ") não é uma opção válida. Por favor, tente novamente com os números da lista";
                }
            }
        } catch (NumberFormatException e) {
            return "Por favor, digite apenas os números dos serviços, separados por vírgula";
        }

        return null;
    }
}
