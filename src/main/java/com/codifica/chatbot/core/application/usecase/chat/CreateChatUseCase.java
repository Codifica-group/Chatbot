package com.codifica.chatbot.core.application.usecase.chat;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ChatRepository;

import java.util.HashMap;
import java.util.Map;

public class CreateChatUseCase {

    private final ChatRepository chatRepository;

    public CreateChatUseCase(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Map<String, Object> execute(Chat chat) {
        Chat novoChat = chatRepository.save(chat);

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Chat cadastrado com sucesso.");
        response.put("id", novoChat.getId());
        return response;
    }
}
