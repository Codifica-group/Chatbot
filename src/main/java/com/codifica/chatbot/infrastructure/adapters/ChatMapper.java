package com.codifica.chatbot.infrastructure.adapters;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.cliente.Cliente;
import com.codifica.chatbot.infrastructure.persistence.chat.ChatEntity;
import com.codifica.chatbot.infrastructure.persistence.cliente.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public ChatEntity toEntity(Chat domain) {
        ChatEntity entity = new ChatEntity();
        entity.setId(domain.getId());
        entity.setPassoAtual(domain.getPassoAtual());
        entity.setDadosContexto(domain.getDadosContexto());
        entity.setDataAtualizacao(domain.getDataAtualizacao());

        if (domain.getCliente() != null && domain.getCliente().getId() != null) {
            ClienteEntity clienteEntity = new ClienteEntity();
            clienteEntity.setId(domain.getCliente().getId());
            clienteEntity.setNome(domain.getCliente().getNome());
            entity.setCliente(clienteEntity);
        }

        return entity;
    }

    public Chat toDomain(ChatEntity entity) {
        Chat domain = new Chat();
        domain.setId(entity.getId());
        domain.setPassoAtual(entity.getPassoAtual());
        domain.setDadosContexto(entity.getDadosContexto());
        domain.setDataAtualizacao(entity.getDataAtualizacao());
        if (entity.getCliente() != null) {
            domain.setCliente(new Cliente(entity.getCliente().getId(), entity.getCliente().getNome()));
        }
        return domain;
    }
}
