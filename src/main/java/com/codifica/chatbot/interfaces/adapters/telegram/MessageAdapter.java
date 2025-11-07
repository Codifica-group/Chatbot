package com.codifica.chatbot.interfaces.adapters.telegram;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Profile("telegram")
public class MessageAdapter {

    private final Pattern inlineButtonPattern = Pattern.compile("^(\\d+) - (.*)$", Pattern.MULTILINE);
    private final Pattern confirmationButtonPattern = Pattern.compile("^(\\d+)\\. (.*)$", Pattern.MULTILINE);

     // Adapta mensagens de texto para botões do Telegram
    public void adaptMessage(SendMessage sendMessage, String nextStep, String messageText) {
        switch (nextStep) {
            // Caso 1: Solicitar número de telefone
            case "AGUARDANDO_TELEFONE_CLIENTE":
                String greeting = "";
                if (messageText.contains("!")) {
                    greeting = messageText.substring(0, messageText.indexOf('!') + 1);
                }

                String newInstruction = "Para continuar, por favor, compartilhe seu contato clicando no botão abaixo. É mais rápido e seguro! 📱";

                if (!greeting.isEmpty()) {
                    sendMessage.setText(greeting + "\n\n" + newInstruction);
                } else {
                    sendMessage.setText(newInstruction);
                }

                sendMessage.setReplyMarkup(buildContactRequestKeyboard());
                break;

            // Caso 2: Listagens genéricas (Pets, Dias, Horários, Raças)
            case "AGUARDANDO_ESCOLHA_PET":
            case "AGUARDANDO_ESCOLHA_DIA":
            case "AGUARDANDO_ESCOLHA_HORARIO":
            case "AGUARDANDO_ESCOLHA_RACA_SUGESTAO":
                InlineKeyboardMarkup inlineList = buildInlineKeyboardFromList(messageText, inlineButtonPattern);
                if (inlineList != null) {
                    String prompt = messageText.split("\n")[0];
                    sendMessage.setText(prompt);
                    sendMessage.setReplyMarkup(inlineList);
                }
                break;

            // Caso 3: Confirmação de Agendamento (1. Confirmar / 2. Recusar)
            case "AGUARDANDO_RESPOSTA_SOLICITACAO_CLIENTE":
                InlineKeyboardMarkup confirmationList = buildInlineKeyboardFromList(messageText, confirmationButtonPattern);
                if (confirmationList != null) {
                    int confirmationPromptIndex = messageText.lastIndexOf("Por favor, confirme o agendamento:");

                    if (confirmationPromptIndex != -1) {
                        sendMessage.setText(messageText.substring(0, confirmationPromptIndex).trim());
                    }

                    sendMessage.setReplyMarkup(confirmationList);
                }
                break;

            // Caso 4: Flag especial para remover o ReplyKeyboard (do telefone)
            case "REMOVE_KEYBOARD":
                sendMessage.setReplyMarkup(buildRemoveKeyboard());
                break;

            // Default: Nenhuma adaptação necessária
            default:
                break;
        }
    }

    private InlineKeyboardMarkup buildInlineKeyboardFromList(String message, Pattern pattern) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String callbackData = matcher.group(1);
            String text = matcher.group(2);

            InlineKeyboardButton button = InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(callbackData)
                    .build();
            rows.add(List.of(button));
        }

        if (rows.isEmpty()) {
            return null;
        }

        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    private ReplyKeyboardMarkup buildContactRequestKeyboard() {
        KeyboardButton phoneButton = new KeyboardButton("Compartilhar meu número 📱");
        phoneButton.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(phoneButton);
        List<KeyboardRow> keyboard = List.of(row);

        ReplyKeyboardMarkup phoneKeyboard = new ReplyKeyboardMarkup();
        phoneKeyboard.setKeyboard(keyboard);
        phoneKeyboard.setResizeKeyboard(true);
        phoneKeyboard.setOneTimeKeyboard(true);
        return phoneKeyboard;
    }

    private ReplyKeyboardRemove buildRemoveKeyboard() {
        return new ReplyKeyboardRemove(true);
    }
}
