package com.codifica.chatbot.core.domain.disponibilidade;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Disponibilidade {
    private LocalDate dia;
    private String diaSemana;
    private List<LocalTime> horarios;

    public LocalDate getDia() {
        return dia;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public List<LocalTime> getHorarios() {
        return horarios;
    }
}
