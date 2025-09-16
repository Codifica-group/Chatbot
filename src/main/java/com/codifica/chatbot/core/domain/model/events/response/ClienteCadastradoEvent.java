package com.codifica.chatbot.core.domain.model.events.response;

import java.io.Serializable;
public class ClienteCadastradoEvent implements Serializable {
    private Integer chatId;
    private Integer clienteId;
    private String status;

    public ClienteCadastradoEvent(Integer chatId, Integer clienteId, String status) {
        this.chatId = chatId;
        this.clienteId = clienteId;
        this.status = status;
    }

    public Integer getChatId() {
        return chatId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public String getStatus() {
        return status;
    }
}
