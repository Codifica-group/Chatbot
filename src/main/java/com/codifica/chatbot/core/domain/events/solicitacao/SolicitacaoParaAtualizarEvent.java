package com.codifica.chatbot.core.domain.events.solicitacao;

import java.io.Serializable;

public class SolicitacaoParaAtualizarEvent implements Serializable {

    private final Integer chatId;
    private final Integer solicitacaoId;
    private final String status;

    public SolicitacaoParaAtualizarEvent(Integer chatId, Integer solicitacaoId, String status) {
        this.chatId = chatId;
        this.solicitacaoId = solicitacaoId;
        this.status = status;
    }

    public Integer getChatId() {
        return chatId;
    }

    public Integer getSolicitacaoId() {
        return solicitacaoId;
    }

    public String getStatus() {
        return status;
    }
}
