package com.codifica.chatbot.core.domain.events.solicitacao;

import com.codifica.chatbot.core.domain.servico.Servico;
import com.codifica.chatbot.core.domain.solicitacao.SolicitacaoAgenda;

import java.io.Serializable;
import java.util.List;

public class SolicitacaoAtualizadaEvent implements Serializable {

    private String status;
    private SolicitacaoAgenda solicitacao;
    private boolean dataInicioAlterada;

    public SolicitacaoAtualizadaEvent(String status, SolicitacaoAgenda solicitacao, boolean dataInicioAlterada) {
        this.status = status;
        this.solicitacao = solicitacao;
        this.dataInicioAlterada = dataInicioAlterada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SolicitacaoAgenda getSolicitacao() {
        return solicitacao;
    }

    public boolean isDataInicioAlterada() {
        return dataInicioAlterada;
    }

    @Override
    public String toString() {
        List<Servico> servicos = solicitacao.getServicos();

        return "Pet: " + solicitacao.getPet().getNome() + "\n" +
                "Servicos: " + servicos + "\n" +
                "Valor Deslocamento: " + solicitacao.getValorDeslocamento() + "\n" +
                "Data Inicio: " + solicitacao.getDataHoraInicio() + "\n" +
                "Data Fim: " + solicitacao.getDataHoraFim() + "\n" +
                "Valor Total: " + solicitacao.getValorTotal() + "\n";
    }
}
