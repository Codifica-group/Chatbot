package com.codifica.chatbot.core.domain.events.solicitacao;

import com.codifica.chatbot.core.domain.shared.StatusEvent;

import java.io.Serializable;

public class SolicitacaoAceitaResponseEvent implements Serializable {

    private StatusEvent status;
    private Long chatId;
    private String erro;

    public StatusEvent getStatus() {
        return status;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getErro() {
        return erro;
    }
}
