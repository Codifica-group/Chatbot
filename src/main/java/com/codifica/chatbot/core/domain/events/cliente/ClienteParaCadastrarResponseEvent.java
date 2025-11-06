package com.codifica.chatbot.core.domain.events.cliente;

import com.codifica.chatbot.core.domain.shared.StatusEvent;

import java.io.Serializable;

public class ClienteParaCadastrarResponseEvent implements Serializable {

    private StatusEvent status;
    private Long chatId;
    private Integer clienteId;
    private String clienteNome;
    private String erro;

    public StatusEvent getStatus() {
        return status;
    }

    public Long getChatId() {
        return chatId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public String getErro() {
        return erro;
    }
}
