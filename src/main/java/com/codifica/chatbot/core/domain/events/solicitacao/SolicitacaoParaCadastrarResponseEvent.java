package com.codifica.chatbot.core.domain.events.solicitacao;

import com.codifica.chatbot.core.domain.shared.StatusEvent;

import java.io.Serializable;

public class SolicitacaoParaCadastrarResponseEvent implements Serializable {

    private StatusEvent status;
    private Integer chatId;
    private Integer solicitacaoId;
    private String erro;

    public StatusEvent getStatus() {
        return status;
    }

    public Integer getChatId() {
        return chatId;
    }

    public Integer getSolicitacaoId() {
        return solicitacaoId;
    }

    public String getErro() {
        return erro;
    }
}
