package com.codifica.chatbot.infrastructure.persistence.chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatJpaRepository extends JpaRepository<ChatEntity, Integer> {

}
