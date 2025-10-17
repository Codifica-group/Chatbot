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

import java.util.List;
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

        try {
            Raca raca = mainBackendService.findRacaByNome(userMessage);
            Map<String, String> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            publishPetEvent(chat.getId(), chat.getCliente().getId(), dadosContexto.get("pet_nome"), raca);
            return new StepResponse("Tudo certo! Cadastrei seu pet. Agora vamos para o agendamento.", "AGUARDANDO_RESPOSTA_CADASTRO_PET");

        } catch (HttpClientErrorException.NotFound e) {
            List<Raca> sugestoes = mainBackendService.findRacasByNomeSemelhante(userMessage);
            if (sugestoes == null || sugestoes.isEmpty()) {
                return new StepResponse("Não encontrei essa raça e nenhuma parecida. Por favor, digite o nome da raça novamente.", getStepName());
            }

            try {
                Map<String, Object> context = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
                context.put("sugestoesRaca", sugestoes);
                chat.setDadosContexto(objectMapper.writeValueAsString(context));

                StringBuilder response = new StringBuilder("Não encontrei essa raça. Você quis dizer uma destas?\n");
                for (int i = 0; i < sugestoes.size(); i++) {
                    response.append(String.format("%d - %s\n", i + 1, sugestoes.get(i).getNome()));
                }
                response.append(String.format("%d - Nenhuma das opções, digitar novamente.", sugestoes.size() + 1));
                return new StepResponse(response.toString(), "AGUARDANDO_ESCOLHA_RACA_SUGESTAO");

            } catch (Exception ex) {
                return new StepResponse("Ocorreu um erro ao processar as sugestões de raça. Tente novamente.", getStepName());
            }
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar a raça do seu pet. Tente novamente.", getStepName());
        }
    }

    private void publishPetEvent(Integer chatId, Integer clienteId, String petNome, Raca raca) {
        PetParaCadastrarEvent event = new PetParaCadastrarEvent(chatId, clienteId, petNome, raca);
        petEventPublisherPort.publishPetParaCadastrar(event);
    }
}
