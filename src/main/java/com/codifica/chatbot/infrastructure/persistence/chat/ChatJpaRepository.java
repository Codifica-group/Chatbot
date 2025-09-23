package com.codifica.chatbot.infrastructure.persistence.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatJpaRepository extends JpaRepository<ChatEntity, Integer> {

    @Query("SELECT c FROM ChatEntity c LEFT JOIN FETCH c.chatCliente")
    List<ChatEntity> findAllWithCliente();
}
