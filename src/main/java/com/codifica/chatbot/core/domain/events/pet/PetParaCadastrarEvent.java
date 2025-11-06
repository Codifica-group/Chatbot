package com.codifica.chatbot.core.domain.events.pet;

import com.codifica.chatbot.core.domain.raca.Raca;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PetParaCadastrarEvent implements Serializable {

    private final Long chatId;
    private final Integer clienteId;
    private final String nome;
    private final Raca raca;

    @JsonCreator
    public PetParaCadastrarEvent(
            @JsonProperty("chatId") Long chatId,
            @JsonProperty("clienteId") Integer clienteId,
            @JsonProperty("nome") String nome,
            @JsonProperty("raca") Raca raca) {
        this.chatId = chatId;
        this.clienteId = clienteId;
        this.nome = nome;
        this.raca = raca;
    }

    public Long getChatId() {
        return chatId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public String getNome() {
        return nome;
    }

    public Raca getRaca() {
        return raca;
    }
}
