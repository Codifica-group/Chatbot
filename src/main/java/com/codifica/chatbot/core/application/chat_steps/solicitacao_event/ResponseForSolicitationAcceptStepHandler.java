package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseForSolicitationAcceptStepHandler implements ConversationStep {

    @Override
    public String getStepName() {
        return "AGENDAMENTO_CONFIRMADO";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            boolean aceito = objectMapper.readTree(chat.getDadosContexto()).get("aceito").asBoolean();

            String responseMessage;
            if (aceito) {
                responseMessage = "Agendamento confirmado com sucesso! Esperamos por você.";
            } else {
                responseMessage = "Agendamento recusado. Se precisar de mais alguma coisa, é só chamar!";
            }
            return new StepResponse(responseMessage, "FIM");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao exibir a mensagem final." + e, "FIM");
        }
    }
}
