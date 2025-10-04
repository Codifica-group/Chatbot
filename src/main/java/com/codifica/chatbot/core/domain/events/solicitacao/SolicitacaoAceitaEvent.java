package com.codifica.chatbot.core.domain.events.solicitacao;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SolicitacaoAceitaEvent implements Serializable {

    private final Integer chatId;
    private final Integer solicitacaoId;
    private final LocalDateTime dataHoraAtualizacao;
    private final boolean aceito;

    public SolicitacaoAceitaEvent(Integer chatId, Integer solicitacaoId, LocalDateTime dataHoraAtualizacao, boolean aceito) {
        this.chatId = chatId;
        this.solicitacaoId = solicitacaoId;
        this.dataHoraAtualizacao = dataHoraAtualizacao;
        this.aceito = aceito;
    }

    public Integer getChatId() {
        return chatId;
    }

    public Integer getSolicitacaoId() {
        return solicitacaoId;
    }

    public LocalDateTime getDataHoraAtualizacao() {
        return dataHoraAtualizacao;
    }

    public boolean isAceito() {
        return aceito;
    }
}
