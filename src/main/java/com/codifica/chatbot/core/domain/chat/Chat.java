package com.codifica.chatbot.core.domain.chat;

import com.codifica.chatbot.core.domain.cliente.Cliente;
import java.time.LocalDateTime;

public class Chat {
    private Integer id;
    private String passoAtual;
    private String dadosContexto;
    private LocalDateTime dataAtualizacao;
    private Cliente cliente;

    public Chat(Integer id, String passoAtual, String dadosContexto, LocalDateTime dataAtualizacao, Cliente cliente) {
        this.id = id;
        this.passoAtual = passoAtual;
        this.dadosContexto = dadosContexto;
        this.dataAtualizacao = dataAtualizacao;
        this.cliente = cliente;
    }

    public Chat() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassoAtual() {
        return passoAtual;
    }

    public void setPassoAtual(String passoAtual) {
        this.passoAtual = passoAtual;
    }

    public String getDadosContexto() {
        return dadosContexto;
    }

    public void setDadosContexto(String dadosContexto) {
        this.dadosContexto = dadosContexto;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
