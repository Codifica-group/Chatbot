package com.codifica.chatbot.core.domain.chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    Chat save(Chat chat);
    Optional<Chat> findById(Integer id);
    List<Chat> findAll();
    void deleteById(Integer id);
}
