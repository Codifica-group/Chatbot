package com.codifica.chatbot.core.domain.events.cliente;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ClienteParaCadastrarEvent implements Serializable {

    private final Long chatId;
    private final String nome;
    private final String telefone;
    private final String cep;
    private final String rua;
    private final String bairro;
    private final String cidade;
    private final String numeroEndereco;
    private final String complemento;

    @JsonCreator
    public ClienteParaCadastrarEvent(
            @JsonProperty("chatId") Long chatId,
            @JsonProperty("nome") String nome,
            @JsonProperty("telefone") String telefone,
            @JsonProperty("cep") String cep,
            @JsonProperty("rua") String rua,
            @JsonProperty("bairro") String bairro,
            @JsonProperty("cidade") String cidade,
            @JsonProperty("numeroEndereco") String numeroEndereco,
            @JsonProperty("complemento") String complemento) {
        this.chatId = chatId;
        this.nome = nome;
        this.telefone = telefone;
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.numeroEndereco = numeroEndereco;
        this.complemento = complemento;
    }

    public Long getChatId() {
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

    public String getRua() {
        return rua;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getNumeroEndereco() {
        return numeroEndereco;
    }

    public String getComplemento() {
        return complemento;
    }
}
