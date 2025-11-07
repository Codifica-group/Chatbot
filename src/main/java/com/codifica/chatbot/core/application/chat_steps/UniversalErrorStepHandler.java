package com.codifica.chatbot.core.application.chat_steps;

import com.codifica.chatbot.core.application.usecase.chat.SafeResetStepUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;

public class UniversalErrorStepHandler implements ConversationStep {

    @Override
    public String getStepName() {
        return "ERRO";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {

        String safeStep = SafeResetStepUseCase.execute(chat);
        String responseMessage = "Desculpe, ocorreu um erro inesperado e não consegui processar sua mensagem 😥 Já registramos o problema! Por favor, tente novamente mais tarde.";

        return new StepResponse(responseMessage, safeStep);
    }
}
