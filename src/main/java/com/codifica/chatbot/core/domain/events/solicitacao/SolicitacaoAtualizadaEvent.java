package com.codifica.chatbot.core.domain.events.solicitacao;

import com.codifica.chatbot.core.domain.solicitacao.SolicitacaoAgenda;
import java.io.Serializable;

public class SolicitacaoAtualizadaEvent implements Serializable {

    private Boolean aceito;
    private Boolean dataInicioAlterada;
    private SolicitacaoAgenda solicitacao;

    public SolicitacaoAtualizadaEvent(Boolean aceito, Boolean dataInicioAlterada, SolicitacaoAgenda solicitacao) {
        this.aceito = aceito;
        this.dataInicioAlterada = dataInicioAlterada;
        this.solicitacao = solicitacao;
    }

    public SolicitacaoAtualizadaEvent() {
    }

    public Boolean getAceito() {
        return aceito;
    }

    public void setAceito(Boolean aceito) {
        this.aceito = aceito;
    }

    public Boolean getDataInicioAlterada() {
        return dataInicioAlterada;
    }

    public SolicitacaoAgenda getSolicitacao() {
        return solicitacao;
    }
}
