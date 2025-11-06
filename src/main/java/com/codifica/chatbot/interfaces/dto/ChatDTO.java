package com.codifica.chatbot.interfaces.dto;

import java.time.LocalDateTime;

public class ChatDTO {
    private Long id;
    private String passoAtual;
    private String dadosContexto;
    private LocalDateTime dataAtualizacao;
    private Integer clienteId;
    private String clienteNome;

    public ChatDTO(Long id, String passoAtual, String dadosContexto, LocalDateTime dataAtualizacao, Integer clienteId, String clienteNome) {
        this.id = id;
        this.passoAtual = passoAtual;
        this.dadosContexto = dadosContexto;
        this.dataAtualizacao = dataAtualizacao;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
    }

    public ChatDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }
}
