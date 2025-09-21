package com.codifica.chatbot.core.domain.events.solicitacao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

public class SolicitacaoServicoEvent implements Serializable {

    private final Integer id;
    private final BigDecimal valor;

    @JsonCreator
    public SolicitacaoServicoEvent(
            @JsonProperty("id") Integer id,
            @JsonProperty("valor") BigDecimal valor) {
        this.id = id;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
