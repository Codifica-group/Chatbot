package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.pet.Pet;
import com.codifica.chatbot.infrastructure.services.MainBackendService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class AskForPetStepHandler implements ConversationStep {

    private final MainBackendService mainBackendService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public AskForPetStepHandler(MainBackendService mainBackendService) {
        this.mainBackendService = mainBackendService;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_AGENDAMENTO";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        List<Pet> pets = mainBackendService.listPetsByClienteId(chat.getCliente().getId());
        StringBuilder response = new StringBuilder("Selecione um de seus pets para o agendamento:\n");
        for (int i = 0; i < pets.size(); i++) {
            response.append(String.format("%d - %s\n", i + 1, pets.get(i).getNome()));
        }
        response.append(String.format("%d - Cadastrar um novo pet", pets.size() + 1));

        try {
            chat.setDadosContexto(objectMapper.writeValueAsString(Map.of("pets", pets)));
        } catch (JsonProcessingException e) {
            return new StepResponse("Ocorreu um erro ao processar a lista de pets. Tente novamente.", getStepName());
        }

        return new StepResponse(response.toString(), "AGUARDANDO_ESCOLHA_PET");
    }
}
