package com.codifica.chatbot.core.application.chat_steps.pet_event;

import com.codifica.chatbot.core.application.ports.out.PetEventPublisherPort;
import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarEvent;
import com.codifica.chatbot.core.domain.raca.Raca;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class AskForBreedSuggestionStepHandler implements ConversationStep {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PetEventPublisherPort petEventPublisherPort;

    public AskForBreedSuggestionStepHandler(PetEventPublisherPort petEventPublisherPort) {
        this.petEventPublisherPort = petEventPublisherPort;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_ESCOLHA_RACA_SUGESTAO";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, Object> context = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            List<Raca> sugestoes = objectMapper.convertValue(context.get("sugestoesRaca"), new TypeReference<List<Raca>>() {});

            String validationError = ValidationUtil.validateIntegerChoice(userMessage, sugestoes.size() + 1);
            if (validationError != null) {
                return new StepResponse(validationError, getStepName());
            }

            int choice = Integer.parseInt(userMessage.trim()) - 1;

            if (choice == sugestoes.size()) {
                context.remove("sugestoesRaca");
                chat.setDadosContexto(objectMapper.writeValueAsString(context));
                return new StepResponse("Entendido. Por favor, digite o nome da raça novamente.", "AGUARDANDO_RACA_PET");
            }

            Raca racaEscolhida = sugestoes.get(choice);
            String petNome = (String) context.get("pet_nome");

            publishPetEvent(chat.getId(), chat.getCliente().getId(), petNome, racaEscolhida);

            return new StepResponse("Tudo certo! Cadastrei seu pet. Agora vamos para o agendamento.", "AGUARDANDO_RESPOSTA_CADASTRO_PET");

        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar sua escolha. Tente novamente.", "AGUARDANDO_RACA_PET");
        }
    }

    private void publishPetEvent(Integer chatId, Integer clienteId, String petNome, Raca raca) {
        PetParaCadastrarEvent event = new PetParaCadastrarEvent(chatId, clienteId, petNome, raca);
        petEventPublisherPort.publishPetParaCadastrar(event);
    }
}
