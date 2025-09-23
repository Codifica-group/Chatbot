package com.codifica.chatbot.infrastructure.persistence.chat_cliente;

import com.codifica.chatbot.infrastructure.persistence.chat.ChatEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "chat_cliente")
public class ChatClienteEntity {

    @Id
    @Column(name = "chat_id")
    private Integer chatId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;

    @Column(name = "cliente_id", nullable = false, unique = true)
    private Integer clienteId;

    public ChatClienteEntity() {
    }

    public ChatClienteEntity(ChatEntity chat, Integer clienteId) {
        this.chat = chat;
        this.clienteId = clienteId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public ChatEntity getChat() {
        return chat;
    }

    public void setChat(ChatEntity chat) {
        this.chat = chat;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
}
