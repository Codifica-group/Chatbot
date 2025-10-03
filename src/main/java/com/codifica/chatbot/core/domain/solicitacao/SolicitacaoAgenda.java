package com.codifica.chatbot.core.domain.solicitacao;

import com.codifica.chatbot.core.domain.pet.Pet;
import com.codifica.chatbot.core.domain.servico.Servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SolicitacaoAgenda implements Serializable {

    private Integer id;
    private Integer chatId;
    private Pet pet;
    private List<Servico> servicos;
    private Double valorDeslocamento;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private LocalDateTime dataHoraSolicitacao;
    private BigDecimal valorTotal;
    private String status;

    public Integer getId() {
        return id;
    }

    public Integer getChatId() {
        return chatId;
    }

    public Pet getPet() {
        return pet;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public Double getValorDeslocamento() {
        return valorDeslocamento;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public LocalDateTime getDataHoraSolicitacao() {
        return dataHoraSolicitacao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public String getStatus() {
        return status;
    }
}
