package com.codifica.chatbot.core.application.chat_steps.pet_event;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class AskForPetBreedStepHandler implements ConversationStep {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getStepName() {
        return "AGUARDANDO_NOME_PET";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, String> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            dadosContexto.put("pet_nome", userMessage);
            chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));

            String responseMessage = "Que nome legal! E qual é a raça do(a) " + userMessage + "?";
            return new StepResponse(responseMessage, "AGUARDANDO_RACA_PET");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar o nome do seu pet. Tente novamente.", getStepName());
        }
    }
}
