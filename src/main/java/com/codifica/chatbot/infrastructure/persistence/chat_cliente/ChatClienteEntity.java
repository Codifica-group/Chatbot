package com.codifica.chatbot.infrastructure.persistence.chat_cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_cliente")
public class ChatClienteEntity {

    @Id
    @Column(name = "chat_id")
    private Integer chatId;

    @Column(name = "cliente_id", nullable = false, unique = true)
    private Integer clienteId;

    public ChatClienteEntity() {
    }

    public ChatClienteEntity(Integer chatId, Integer clienteId) {
        this.chatId = chatId;
        this.clienteId = clienteId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
}
