package com.codifica.chatbot.core.application.usecase.chat;

import com.codifica.chatbot.core.domain.chat.ChatRepository;

public class DeleteChatUseCase {

    private final ChatRepository chatRepository;

    public DeleteChatUseCase(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public void execute(Integer id) {
        chatRepository.deleteById(id);
    }
}
