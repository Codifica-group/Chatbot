package com.codifica.chatbot.core.domain.shared;

import java.io.Serializable;

public class Endereco implements Serializable {

    private final String cep;
    private final String rua;
    private final String numero;
    private final String bairro;
    private final String cidade;
    private final String complemento;

    public Endereco(String cep, String rua, String numero, String bairro, String cidade, String complemento) {
        if (cep == null || !cep.matches("\\d{8}")) {
            throw new IllegalArgumentException("O CEP deve conter 8 dígitos numéricos.");
        }
        if (rua == null || rua.trim().isEmpty()) {
            throw new IllegalArgumentException("O campo Rua não pode ser vazio.");
        }
        if (bairro == null || bairro.trim().isEmpty()) {
            throw new IllegalArgumentException("O campo Bairro não pode ser vazio.");
        }
        if (cidade == null || cidade.trim().isEmpty()) {
            throw new IllegalArgumentException("O campo Cidade não pode ser vazio.");
        }

        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.complemento = complemento;
    }

    public String getCep() {
        return cep;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getComplemento() {
        return complemento;
    }
}
