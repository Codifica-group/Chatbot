package com.codifica.chatbot.core.application.chat_steps.cliente_event;

import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class AskForAdressNumberStepHandler implements ConversationStep {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getStepName() {
        return "AGUARDANDO_CEP_CLIENTE";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        String validationError = ValidationUtil.validateCep(userMessage);
        if (validationError != null) {
            return new StepResponse(validationError, getStepName());
        }

        try {
            Map<String, String> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            dadosContexto.put("cep", userMessage);
            chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));

            String responseMessage = "Entendi! Qual o número do seu endereço?";
            return new StepResponse(responseMessage, "AGUARDANDO_NUMERO_ENDERECO_CLIENTE");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar seu cep. Tente novamente.", getStepName());
        }
    }
}
