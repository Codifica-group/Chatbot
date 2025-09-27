package com.codifica.chatbot.core.domain.pet;

public class Pet {

    private Integer id;
    private String nome;

    public Pet(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
