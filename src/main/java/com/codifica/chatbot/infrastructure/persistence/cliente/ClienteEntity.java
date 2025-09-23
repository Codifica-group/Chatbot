package com.codifica.chatbot.infrastructure.persistence.cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente")
public class ClienteEntity {

    @Id
    private Integer id;

    private String nome;

    public ClienteEntity(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public ClienteEntity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
