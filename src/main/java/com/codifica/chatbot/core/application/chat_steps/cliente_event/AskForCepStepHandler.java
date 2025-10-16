package com.codifica.chatbot.core.application.chat_steps.cliente_event;

import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class AskForCepStepHandler implements ConversationStep {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getStepName() {
        return "AGUARDANDO_TELEFONE_CLIENTE";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        String validationError = ValidationUtil.validatePhoneNumber(userMessage);
        if (validationError != null) {
            return new StepResponse(validationError, getStepName());
        }

        try {
            Map<String, String> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            dadosContexto.put("telefone", userMessage);
            chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));

            String responseMessage = "Ok! E qual o seu CEP?";
            return new StepResponse(responseMessage, "AGUARDANDO_CEP_CLIENTE");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar seu telefone. Tente novamente.", getStepName());
        }
    }
}
