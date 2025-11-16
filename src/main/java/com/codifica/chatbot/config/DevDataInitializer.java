package com.codifica.chatbot.config;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.cliente.Cliente;
import com.codifica.chatbot.infrastructure.adapters.ChatMapper;
import com.codifica.chatbot.infrastructure.persistence.chat.ChatEntity;
import com.codifica.chatbot.infrastructure.persistence.chat.ChatJpaRepository;
import com.codifica.chatbot.infrastructure.persistence.cliente.ClienteEntity;
import com.codifica.chatbot.infrastructure.persistence.cliente.ClienteJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Configuration
@Profile("db-dev")
public class DevDataInitializer {

    @Autowired
    private ClienteJpaRepository clienteJpaRepository;

    @Autowired
    private ChatJpaRepository chatJpaRepository;

    @Autowired
    private ChatMapper chatMapper;

    public void initDatabase() {
        ClienteEntity clienteEntity1 = new ClienteEntity(1, "Cliente Teste");
        clienteJpaRepository.save(clienteEntity1);

        ClienteEntity clienteEntity2 = new ClienteEntity(2, "Cliente Teste 2");
        clienteJpaRepository.save(clienteEntity2);

        Cliente cliente1 = new Cliente(clienteEntity1.getId(), clienteEntity1.getNome());
        Cliente cliente2 = new Cliente(clienteEntity2.getId(), clienteEntity2.getNome());

        Chat chat1 = new Chat();
        chat1.setId(System.currentTimeMillis());
        chat1.setPassoAtual("AGUARDANDO_AGENDAMENTO");
        chat1.setDadosContexto("{}");
        chat1.setCliente(cliente1);
        chat1.setDataAtualizacao(LocalDateTime.now());
        save(chat1);

        Chat chat2 = new Chat();
        chat2.setId(System.currentTimeMillis() + 1);
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
