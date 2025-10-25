package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.disponibilidade.Disponibilidade;
import com.codifica.chatbot.core.domain.servico.Servico;
import com.codifica.chatbot.infrastructure.services.MainBackendService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ListServicesStepHandler implements ConversationStep {

    private final MainBackendService mainBackendService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Logger logger = LoggerFactory.getLogger(ListServicesStepHandler.class);

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
            List<Disponibilidade> disponibilidade = objectMapper.convertValue(context.get("disponibilidade"), new TypeReference<List<Disponibilidade>>() {});
            Integer diaEscolhidoIndex = objectMapper.convertValue(context.get("diaEscolhido"), Integer.class);
            List<LocalTime> availableTimes = disponibilidade.get(diaEscolhidoIndex).getHorarios();

            String validationError = ValidationUtil.validateIntegerChoice(userMessage, availableTimes.size() + 1);
            if (validationError != null) {
                return new StepResponse(validationError, getStepName());
            }

            int timeChoice = Integer.parseInt(userMessage.trim()) - 1;

            if (timeChoice == availableTimes.size()) {
                context.remove("diaEscolhido");
                chat.setDadosContexto(objectMapper.writeValueAsString(context));

                StringBuilder response = new StringBuilder("Sem problemas! Aqui estão mais alguns dias disponíveis:\n");
                for (int i = 0; i < disponibilidade.size(); i++) {
                    response.append(String.format("%d - %s - %s\n",
                            i + 1,
                            disponibilidade.get(i).getDia().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            disponibilidade.get(i).getDiaSemana()));
                }
                response.append(String.format("%d - Ver mais datas", disponibilidade.size() + 1));
                return new StepResponse(response.toString(), "AGUARDANDO_ESCOLHA_DIA");
            }

            LocalTime chosenTime = availableTimes.get(timeChoice);
            context.put("horarioEscolhido", chosenTime.toString());

            List<Servico> servicos = mainBackendService.getServicos();
            context.put("listaServicos", servicos);
            chat.setDadosContexto(objectMapper.writeValueAsString(context));

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
