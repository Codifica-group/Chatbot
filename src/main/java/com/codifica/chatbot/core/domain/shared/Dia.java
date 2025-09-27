package com.codifica.chatbot.core.domain.shared;

import java.time.LocalDate;

public class Dia {

    private LocalDate data;
    private String diaSemana;

    public Dia(LocalDate data, String diaSemana) {
        this.data = data;
        this.diaSemana = diaSemana;
    }

    public Dia() {}

    public LocalDate getData() {
        return data;
    }

    public String getDiaSemana() {
        return diaSemana;
    }
}
