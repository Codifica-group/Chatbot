package com.codifica.chatbot.core.application.chat_steps.pet_event;

import com.codifica.chatbot.core.application.ports.out.PetEventPublisherPort;
import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class WaitingForPetEventStepHandler implements ConversationStep {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PetEventPublisherPort petEventPublisherPort;

    public WaitingForPetEventStepHandler(PetEventPublisherPort petEventPublisherPort) {
        this.petEventPublisherPort = petEventPublisherPort;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_RACA_PET";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        String validationError = ValidationUtil.validate(userMessage);
        if (validationError != null) {
            return new StepResponse(validationError, getStepName());
        }

        try {
            Map<String, String> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            dadosContexto.put("pet_raca", userMessage);
            chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));

            publishPetEvent(chat.getId(), chat.getCliente().getId(), dadosContexto);

            String responseMessage = "Tudo certo! Cadastrei seu pet. Agora vamos para o agendamento.";
            return new StepResponse(responseMessage, "AGUARDANDO_RESPOSTA_CADASTRO_PET");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar a raça do seu pet. Tente novamente.", getStepName());
        }
    }

    private void publishPetEvent(Integer chatId, Integer clienteId, Map<String, String> dados) {
        PetParaCadastrarEvent event = new PetParaCadastrarEvent(
                chatId,
                clienteId,
                dados.get("pet_nome"),
                dados.get("pet_raca")
        );
        petEventPublisherPort.publishPetParaCadastrar(event);
    }
}
