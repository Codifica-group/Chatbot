package com.codifica.chatbot.infrastructure.persistence.chat;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ChatRepository;
import com.codifica.chatbot.infrastructure.adapters.ChatMapper;
import com.codifica.chatbot.infrastructure.persistence.cliente.ClienteEntity;
import com.codifica.chatbot.infrastructure.persistence.cliente.ClienteJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ChatRepositoryImpl implements ChatRepository {

    private final ChatJpaRepository chatJpaRepository;
    private final ClienteJpaRepository clienteJpaRepository;
    private final ChatMapper chatMapper;

    public ChatRepositoryImpl(ChatJpaRepository chatJpaRepository,
                              ClienteJpaRepository clienteJpaRepository,
                              ChatMapper chatMapper) {
        this.chatJpaRepository = chatJpaRepository;
        this.clienteJpaRepository = clienteJpaRepository;
        this.chatMapper = chatMapper;
    }

    @Override
    public Chat save(Chat chat) {
        ChatEntity entity = chatMapper.toEntity(chat);
        if (entity.getCliente() != null) {
            Optional<ClienteEntity> existingCliente = clienteJpaRepository.findById(entity.getCliente().getId());
            if (existingCliente.isEmpty()) {
                clienteJpaRepository.save(entity.getCliente());
            }
        }
        ChatEntity savedEntity = chatJpaRepository.save(entity);
        return chatMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Chat> findById(Long id) {
        return chatJpaRepository.findById(id).map(chatMapper::toDomain);
    }

    @Override
    public List<Chat> findAll() {
        return chatJpaRepository.findAll().stream()
                .map(chatMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        chatJpaRepository.deleteById(id);
    }
}
