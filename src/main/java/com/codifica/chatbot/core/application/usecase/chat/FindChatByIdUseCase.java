package com.codifica.chatbot.core.application.usecase.chat;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ChatRepository;

import java.util.Optional;

public class FindChatByIdUseCase {

    private final ChatRepository chatRepository;

    public FindChatByIdUseCase(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Optional<Chat> execute(Integer id) {
        return chatRepository.findById(id);
    }
}
