package com.codifica.chatbot.infrastructure.persistence.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Integer> {

}
