package com.codifica.chatbot.interfaces.mappers;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.interfaces.dto.ChatDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChatDtoMapper {
    public ChatDTO toDto(Chat domain) {
        return new ChatDTO(
                domain.getId(),
                domain.getPassoAtual(),
                domain.getDadosContexto(),
                domain.getDataAtualizacao(),
                domain.getClienteId()
        );
    }

    public Chat toDomain(ChatDTO dto) {
        LocalDateTime dataAtualizacao = dto.getDataAtualizacao() != null ? dto.getDataAtualizacao() : LocalDateTime.now();
        Chat chat = new Chat(
                dto.getId(),
                dto.getPassoAtual(),
                dto.getDadosContexto(),
                dataAtualizacao,
                dto.getClienteId()
        );

        if (dto.getId() == null) {
            chat.setId(null);
        }
        return chat;
    }
}
