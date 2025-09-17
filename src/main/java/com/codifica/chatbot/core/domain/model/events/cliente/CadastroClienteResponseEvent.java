package com.codifica.chatbot.core.domain.model.events.cliente;

import com.codifica.chatbot.core.domain.shared.StatusEvent;

import java.io.Serializable;

public class CadastroClienteResponseEvent implements Serializable {

    private StatusEvent status;
    private Integer chatId;
    private Integer clienteId;
    private String erro;

    public StatusEvent getStatus() {
        return status;
    }

    public void setStatus(StatusEvent status) {
        this.status = status;
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

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }
}
