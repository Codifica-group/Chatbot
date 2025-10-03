package com.codifica.chatbot.core.domain.shared;

import java.time.DayOfWeek;
import java.time.LocalDate;

public enum DiaDaSemana {
    SEGUNDA_FEIRA("Segunda-feira"),
    TERCA_FEIRA("Terça-feira"),
    QUARTA_FEIRA("Quarta-feira"),
    QUINTA_FEIRA("Quinta-feira"),
    SEXTA_FEIRA("Sexta-feira"),
    SABADO("Sábado"),
    DOMINGO("Domingo");

    private final String nome;

    DiaDaSemana(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public static String fromDate(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        switch (day) {
            case MONDAY:
                return SEGUNDA_FEIRA.getNome();
            case TUESDAY:
                return TERCA_FEIRA.getNome();
            case WEDNESDAY:
                return QUARTA_FEIRA.getNome();
            case THURSDAY:
                return QUINTA_FEIRA.getNome();
            case FRIDAY:
                return SEXTA_FEIRA.getNome();
            case SATURDAY:
                return SABADO.getNome();
            case SUNDAY:
                return DOMINGO.getNome();
            default:
                throw new IllegalStateException("Valor inesperado: " + day);
        }
    }
}
