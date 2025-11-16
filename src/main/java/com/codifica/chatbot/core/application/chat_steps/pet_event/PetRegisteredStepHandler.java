package com.codifica.chatbot.core.application.chat_steps.pet_event;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;

public class PetRegisteredStepHandler implements ConversationStep {

    @Override
    public String getStepName() {
        return "PET_CADASTRADO_SUCESSO";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        String responseMessage = "Tudo certo! Cadastrei seu pet ✅ Agora vamos para o agendamento.";
        return new StepResponse(responseMessage, "AGUARDANDO_AGENDAMENTO");
    }
}
