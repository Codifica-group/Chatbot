package com.codifica.chatbot.core.application.chat_steps.cliente_event;

import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class AskForAdressComplementStepHandler implements ConversationStep {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getStepName() {
        return "AGUARDANDO_NUMERO_ENDERECO_CLIENTE";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        String validationError = ValidationUtil.validate(userMessage, 10);
        if (validationError != null) {
            return new StepResponse(validationError, getStepName());
        }

        try {
            Map<String, String> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            dadosContexto.put("numeroEndereco", userMessage);
            chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));

            String responseMessage = "Estamos quase lá! O endereço possui complemento? Se não tiver, digite 'não'.";
            return new StepResponse(responseMessage, "AGUARDANDO_COMPLEMENTO_CLIENTE");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar seu número do endereço. Tente novamente.", getStepName());
        }
    }
}
