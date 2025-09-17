package com.codifica.chatbot.core.domain.model.events.cliente;

import com.codifica.chatbot.core.domain.shared.Endereco;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ClienteParaCadastrarEvent implements Serializable {

    private final Integer chatId;
    private final String nome;
    private final String telefone;
    private final Endereco endereco;

    @JsonCreator
    public ClienteParaCadastrarEvent(
            @JsonProperty("chatId") Integer chatId,
            @JsonProperty("nome") String nome,
            @JsonProperty("telefone") String telefone,
            @JsonProperty("endereco") Endereco endereco) {
        this.chatId = chatId;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Integer getChatId() {
        return chatId;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public Endereco getEndereco() {
        return endereco;
    }
}
