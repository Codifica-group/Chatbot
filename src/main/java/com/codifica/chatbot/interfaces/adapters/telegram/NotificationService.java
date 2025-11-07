package com.codifica.chatbot.interfaces.adapters.telegram;

public interface NotificationService {
    void sendMessage(Long chatId, String message);
}
