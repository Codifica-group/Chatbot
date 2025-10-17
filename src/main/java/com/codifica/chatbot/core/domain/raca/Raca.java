package com.codifica.chatbot.core.domain.raca;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Raca implements Serializable {
    private Integer id;
    private String nome;
    private Integer porteId;

    @JsonCreator
    public Raca(
            @JsonProperty("id") Integer id,
            @JsonProperty("nome") String nome,
            @JsonProperty("porteId") Integer porteId) {
        this.id = id;
        this.nome = nome;
        this.porteId = porteId;
    }

    public Raca() {}

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getPorteId() {
        return porteId;
    }
}
