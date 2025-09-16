package com.codifica.chatbot.interfaces.mappers;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.interfaces.dto.ChatDTO;
import org.springframework.stereotype.Component;

@Component
public class ChatDtoMapper {
    public ChatDTO toDto(Chat domain) {
        return new ChatDTO(domain.getId(), domain.getPassoAtual(), domain.getDadosContexto(), domain.getDataAtualizacao(), domain.getClienteId());
    }
}
