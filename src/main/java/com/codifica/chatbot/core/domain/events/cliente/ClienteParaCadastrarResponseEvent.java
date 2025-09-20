package com.codifica.chatbot.core.domain.events.cliente;

import com.codifica.chatbot.core.domain.shared.StatusEvent;

import java.io.Serializable;

public class ClienteParaCadastrarResponseEvent implements Serializable {

    private StatusEvent status;
    private Integer chatId;
    private Integer clienteId;
    private String erro;

    public StatusEvent getStatus() {
        return status;
    }

    public Integer getChatId() {
        return chatId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public String getErro() {
        return erro;
    }
}
