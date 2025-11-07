package com.codifica.chatbot.core.application.chat_steps.cliente_event;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;

public class InitialStepHandler implements ConversationStep {

    @Override
    public String getStepName() {
        return "INICIO";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        String responseMessage = "Olá! Bem-vindo ao nosso pet shop 🐾 Para começar, qual é o seu nome completo?";
        return new StepResponse(responseMessage, "AGUARDANDO_NOME_CLIENTE");
    }
}
