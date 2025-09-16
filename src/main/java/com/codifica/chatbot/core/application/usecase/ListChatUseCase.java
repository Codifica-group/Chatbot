package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ChatRepository;

import java.util.List;

public class ListChatUseCase {

    private final ChatRepository chatRepository;

    public ListChatUseCase(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> execute() {
        return chatRepository.findAll();
    }
}
