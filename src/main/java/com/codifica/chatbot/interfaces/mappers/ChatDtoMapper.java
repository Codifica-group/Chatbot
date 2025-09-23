package com.codifica.chatbot.interfaces.mappers;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.cliente.Cliente;
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
                domain.getCliente() != null ? domain.getCliente().getId() : null,
                domain.getCliente() != null ? domain.getCliente().getNome() : null
        );
    }

    public Chat toDomain(ChatDTO dto) {
        LocalDateTime dataAtualizacao = dto.getDataAtualizacao() != null
                ? dto.getDataAtualizacao()
                : LocalDateTime.now();

        Cliente cliente = dto.getClienteId() != null
                ? new Cliente(dto.getClienteId(), dto.getClienteNome())
                : null;

        Chat chat = new Chat(
                dto.getId(),
                dto.getPassoAtual(),
                dto.getDadosContexto(),
                dataAtualizacao,
                cliente
        );
        if (dto.getId() == null) {
            chat.setId(null);
        }
        return chat;
    }
}
