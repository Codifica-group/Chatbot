package com.codifica.chatbot.core.application.ports.in;

public interface ProcessarCadastroClienteSucessoUseCasePort {
    void processar(Integer chatId, Integer clienteId);
}
