package com.codifica.chatbot.config;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.cliente.Cliente;
import com.codifica.chatbot.infrastructure.adapters.ChatMapper;
import com.codifica.chatbot.infrastructure.persistence.chat.ChatEntity;
import com.codifica.chatbot.infrastructure.persistence.chat.ChatJpaRepository;
import com.codifica.chatbot.infrastructure.persistence.cliente.ClienteJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DevDataInitializer {

    @Autowired
    private ClienteJpaRepository clienteJpaRepository;

    @Autowired
    private ChatJpaRepository chatJpaRepository;

    @Autowired
    private ChatMapper chatMapper;

    public void initDatabase() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        cliente.setId(1);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Cliente Teste 2");
        cliente2.setId(2);

        Chat chat1 = new Chat();
        chat1.setPassoAtual("AGUARDANDO_AGENDAMENTO");
        chat1.setDadosContexto("{}");
        chat1.setCliente(cliente);
        chat1.setDataAtualizacao(LocalDateTime.now());
        save(chat1);

        Chat chat2 = new Chat();
        chat2.setPassoAtual("AGUARDANDO_CADASTRO_PET");
        chat2.setDadosContexto("{}");
        chat2.setCliente(cliente2);
        chat2.setDataAtualizacao(LocalDateTime.now());
        save(chat2);
    }

    private Chat save(Chat chat) {
    ChatEntity entity = chatMapper.toEntity(chat);

    if (entity.getCliente() != null && entity.getCliente().getId() != null) {
        clienteJpaRepository.findById(entity.getCliente().getId())
                .ifPresent(entity::setCliente);
    }

    ChatEntity savedEntity = chatJpaRepository.save(entity);
    return chatMapper.toDomain(savedEntity);
    }
}
