package com.codifica.chatbot.infrastructure.persistence.chat;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ChatRepository;
import com.codifica.chatbot.infrastructure.adapters.ChatMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ChatRepositoryImpl implements ChatRepository {

    private final ChatJpaRepository chatJpaRepository;
    private final ChatMapper chatMapper;

    public ChatRepositoryImpl(ChatJpaRepository chatJpaRepository, ChatMapper chatMapper) {
        this.chatJpaRepository = chatJpaRepository;
        this.chatMapper = chatMapper;
    }

    @Override
    public Chat save(Chat chat) {
        ChatEntity entity = chatMapper.toEntity(chat);
        ChatEntity savedEntity = chatJpaRepository.save(entity);
        return chatMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Chat> findById(Integer id) {
        return chatJpaRepository.findById(id).map(chatMapper::toDomain);
    }

    @Override
    public List<Chat> findAll() {
        return chatJpaRepository.findAll().stream().map(chatMapper::toDomain).collect(Collectors.toList());
    }
}
