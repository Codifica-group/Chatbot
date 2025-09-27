package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.servico.Servico;
import com.codifica.chatbot.infrastructure.services.MainBackendService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class ListServicesStepHandler implements ConversationStep {

    private final MainBackendService mainBackendService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ListServicesStepHandler(MainBackendService mainBackendService) {
        this.mainBackendService = mainBackendService;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_ESCOLHA_HORARIO";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, Object> context = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            context.put("chosenTime", userMessage);
            chat.setDadosContexto(objectMapper.writeValueAsString(context));

            List<Servico> servicos = mainBackendService.getServicos();
            StringBuilder response = new StringBuilder("Ótimo! Agora, digite os números dos serviços que você deseja, separados por vírgula:\n");
            for (int i = 0; i < servicos.size(); i++) {
                response.append(String.format("%d - %s\n", i + 1, servicos.get(i).getNome()));
            }

            return new StepResponse(response.toString(), "AGUARDANDO_ESCOLHA_SERVICOS");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar sua escolha. Tente novamente.", getStepName());
        }
    }
}
