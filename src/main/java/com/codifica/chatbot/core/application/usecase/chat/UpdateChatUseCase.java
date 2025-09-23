package com.codifica.chatbot.core.application.usecase.chat;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ChatRepository;

import java.util.HashMap;
import java.util.Map;

public class UpdateChatUseCase {

    private final ChatRepository chatRepository;

    public UpdateChatUseCase(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Map<String, Object> execute(Integer id, Chat chat) {
        chat.setId(id);
        Chat chatAtualizado = chatRepository.save(chat);

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Chat atualizado com sucesso.");
        response.put("id", chatAtualizado.getId());
        return response;
    }
}
