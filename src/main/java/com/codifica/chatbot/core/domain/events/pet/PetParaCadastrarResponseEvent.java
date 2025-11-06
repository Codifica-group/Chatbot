package com.codifica.chatbot.core.domain.events.pet;

import com.codifica.chatbot.core.domain.shared.StatusEvent;

import java.io.Serializable;

public class PetParaCadastrarResponseEvent implements Serializable {

    private StatusEvent status;
    private Long chatId;
    private Integer clienteId;
    private Integer petId;
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

    public Integer getPetId() {
        return petId;
    }

    public String getErro() {
        return erro;
    }
}
