package com.codifica.chatbot.core.domain.events.solicitacao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SolicitacaoParaCadastrarEvent implements Serializable {

    private final Integer chatId;
    private final Integer petId;
    private final List<SolicitacaoServicoEvent> servicos;
    private final BigDecimal valorDeslocamento;
    private final LocalDateTime dataHoraInicio;
    private final String status;

    @JsonCreator
    public SolicitacaoParaCadastrarEvent(
            @JsonProperty("chatId") Integer chatId,
            @JsonProperty("petId") Integer petId,
            @JsonProperty("servicos") List<SolicitacaoServicoEvent> servicos,
            @JsonProperty("valorDeslocamento") BigDecimal valorDeslocamento,
            @JsonProperty("dataHoraInicio") LocalDateTime dataHoraInicio,
            @JsonProperty("status") String status) {
        this.chatId = chatId;
        this.petId = petId;
        this.servicos = servicos;
        this.valorDeslocamento = valorDeslocamento;
        this.dataHoraInicio = dataHoraInicio;
        this.status = status;
    }

    public Integer getChatId() {
        return chatId;
    }

    public Integer getPetId() {
        return petId;
    }

    public List<SolicitacaoServicoEvent> getServicos() {
        return servicos;
    }

    public BigDecimal getValorDeslocamento() {
        return valorDeslocamento;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public String getStatus() {
        return status;
    }
}
