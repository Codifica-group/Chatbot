package com.codifica.chatbot.core.domain.servico;

import java.math.BigDecimal;

public class Servico {

    private Integer id;
    private String nome;
    private BigDecimal valor;

    public Servico(Integer id, String nome, BigDecimal valor) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
    }

    public Servico() {}

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
