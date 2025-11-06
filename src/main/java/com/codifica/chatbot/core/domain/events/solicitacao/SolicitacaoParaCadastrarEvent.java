package com.codifica.chatbot.core.domain.events.solicitacao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class SolicitacaoParaCadastrarEvent implements Serializable {

    private final Long chatId;
    private final Integer petId;
    private final List<Integer> servicos;
    private final LocalDateTime dataHoraInicio;
    private final LocalDateTime dataHoraSolicitacao;
    private final String status;

    @JsonCreator
    public SolicitacaoParaCadastrarEvent(
            @JsonProperty("chatId") Long chatId,
            @JsonProperty("petId") Integer petId,
            @JsonProperty("servicos") List<Integer> servicos,
            @JsonProperty("dataHoraInicio") LocalDateTime dataHoraInicio,
            @JsonProperty("dataHoraSolicitacao") LocalDateTime dataHoraSolicitacao,
            @JsonProperty("status") String status) {
        this.chatId = chatId;
        this.petId = petId;
        this.servicos = servicos;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraSolicitacao = dataHoraSolicitacao;
        this.status = status;
    }

    public Long getChatId() {
        return chatId;
    }

    public Integer getPetId() {
        return petId;
    }

    public List<Integer> getServicos() {
        return servicos;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public LocalDateTime getDataHoraSolicitacao() {
        return dataHoraSolicitacao;
    }

    public String getStatus() {
        return status;
    }
}
