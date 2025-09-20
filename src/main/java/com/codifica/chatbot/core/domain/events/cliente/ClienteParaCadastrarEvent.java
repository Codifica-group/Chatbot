package com.codifica.chatbot.core.domain.events.cliente;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ClienteParaCadastrarEvent implements Serializable {

    private final Integer chatId;
    private final String nome;
    private final String telefone;
    private final String cep;
    private final String numeroEndereco;
    private final String complemento;

    @JsonCreator
    public ClienteParaCadastrarEvent(
            @JsonProperty("chatId") Integer chatId,
            @JsonProperty("nome") String nome,
            @JsonProperty("telefone") String telefone,
            @JsonProperty("cep") String cep,
            @JsonProperty("numeroEndereco") String numeroEndereco,
            @JsonProperty("complemento") String complemento) {
        this.chatId = chatId;
        this.nome = nome;
        this.telefone = telefone;
        this.cep = cep;
        this.numeroEndereco = numeroEndereco;
        this.complemento = complemento;
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

    public String getCep() {
        return cep;
    }

    public String getNumeroEndereco() {
        return numeroEndereco;
    }

    public String getComplemento() {
        return complemento;
    }
}
