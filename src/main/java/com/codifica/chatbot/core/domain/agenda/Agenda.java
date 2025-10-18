package com.codifica.chatbot.core.domain.agenda;

import com.codifica.chatbot.core.domain.cliente.Cliente;
import com.codifica.chatbot.core.domain.pet.Pet;
import com.codifica.chatbot.core.domain.servico.Servico;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Agenda {

    private Integer id;
    private Integer petId;
    private Cliente cliente;
    private Pet pet;
    private List<Servico> servicos;
    private BigDecimal valorDeslocamento;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private BigDecimal valorTotal;

    public Integer getId() {
        return id;
    }

    public Integer getPetId() {
        return petId;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Pet getPet() {
        return pet;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public BigDecimal getValorDeslocamento() {
        return valorDeslocamento;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
}
