package com.codifica.chatbot.core.domain.solicitacao;

import com.codifica.chatbot.core.domain.cliente.Cliente;
import com.codifica.chatbot.core.domain.pet.Pet;
import com.codifica.chatbot.core.domain.servico.Servico;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SolicitacaoAgenda {

    private Integer id;
    private Integer chatId;
    private Pet pet;
    private Cliente cliente;
    private List<Servico> servicos;
    private BigDecimal valorDeslocamento;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private LocalDateTime dataHoraSolicitacao;
    private BigDecimal valorTotal;
    private String status;

    public SolicitacaoAgenda(Integer id, Integer chatId, Pet pet, Cliente cliente, List<Servico> servicos, BigDecimal valorDeslocamento, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, LocalDateTime dataHoraSolicitacao, BigDecimal valorTotal, String status) {
        this.id = id;
        this.chatId = chatId;
        this.pet = pet;
        this.cliente = cliente;
        this.servicos = servicos;
        this.valorDeslocamento = valorDeslocamento;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.dataHoraSolicitacao = dataHoraSolicitacao;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public SolicitacaoAgenda() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

    public BigDecimal getValorDeslocamento() {
        return valorDeslocamento;
    }

    public void setValorDeslocamento(BigDecimal valorDeslocamento) {
        this.valorDeslocamento = valorDeslocamento;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public LocalDateTime getDataHoraSolicitacao() {
        return dataHoraSolicitacao;
    }

    public void setDataHoraSolicitacao(LocalDateTime dataHoraSolicitacao) {
        this.dataHoraSolicitacao = dataHoraSolicitacao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
