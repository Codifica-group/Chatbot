package com.codifica.chatbot.core.domain.model.events.response;

import java.io.Serializable;

public class FalhaCadastroClienteEvent implements Serializable {
    private Long chatId;
    private String erro;

    public FalhaCadastroClienteEvent(Long chatId, String erro) {
        this.chatId = chatId;
        this.erro = erro;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getErro() {
        return erro;
    }
}
