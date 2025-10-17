package com.codifica.chatbot.core.application.chat_steps.pet_event;

import com.codifica.chatbot.core.application.ports.out.PetEventPublisherPort;
import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarEvent;
import com.codifica.chatbot.core.domain.raca.Raca;
import com.codifica.chatbot.infrastructure.services.MainBackendService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

public class WaitingForPetEventStepHandler implements ConversationStep {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PetEventPublisherPort petEventPublisherPort;
    private final MainBackendService mainBackendService;

    public WaitingForPetEventStepHandler(PetEventPublisherPort petEventPublisherPort, MainBackendService mainBackendService) {
        this.petEventPublisherPort = petEventPublisherPort;
        this.mainBackendService = mainBackendService;
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

        Raca raca;
        try {
            raca = mainBackendService.findRacaByNome(userMessage);
        } catch (HttpClientErrorException.NotFound e) {
            return new StepResponse("Não encontrei essa raça. Por favor, digite o nome da raça novamente.", getStepName());
        }

        try {
            Map<String, String> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});

            publishPetEvent(chat.getId(), chat.getCliente().getId(), dadosContexto.get("pet_nome"), raca);

            String responseMessage = "Tudo certo! Cadastrei seu pet. Agora vamos para o agendamento.";
            return new StepResponse(responseMessage, "AGUARDANDO_RESPOSTA_CADASTRO_PET");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar a raça do seu pet. Tente novamente.", getStepName());
        }
    }

    private void publishPetEvent(Integer chatId, Integer clienteId, String petNome, Raca raca) {
        PetParaCadastrarEvent event = new PetParaCadastrarEvent(
                chatId,
                clienteId,
                petNome,
                raca
        );
        petEventPublisherPort.publishPetParaCadastrar(event);
    }
}
