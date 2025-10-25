package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.disponibilidade.Disponibilidade;
import com.codifica.chatbot.core.domain.shared.Dia;
import com.codifica.chatbot.infrastructure.services.MainBackendService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ListAvailableTimesStepHandler implements ConversationStep {

    private final MainBackendService mainBackendService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Logger logger = LoggerFactory.getLogger(ListAvailableTimesStepHandler.class);

    public ListAvailableTimesStepHandler(MainBackendService mainBackendService) {
        this.mainBackendService = mainBackendService;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_ESCOLHA_DIA";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, Object> context = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            List<Disponibilidade> disponibilidade = objectMapper.convertValue(context.get("disponibilidade"), new TypeReference<List<Disponibilidade>>() {});

            String validationError = ValidationUtil.validateIntegerChoice(userMessage, disponibilidade.size() + 1);
            if (validationError != null) {
                return new StepResponse(validationError, getStepName());
            }

            int choice = Integer.parseInt(userMessage) - 1;

            if (choice == disponibilidade.size()) {
                LocalDateTime lastFetchedDate = LocalDateTime.parse((String) context.get("ultimoDia"));
                LocalDateTime nextStartDate = lastFetchedDate.plusDays(1);
                LocalDateTime nextEndDate = nextStartDate.plusDays(14);

                disponibilidade = mainBackendService.getDisponibilidade(nextStartDate, nextEndDate);

                if (disponibilidade.isEmpty()) { // TODO: tratar caso nenhum dia disponivel em 14 dias
                    return new StepResponse("Não encontrei mais datas disponíveis após " + lastFetchedDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ". Escolha uma das datas anteriores ou tente novamente mais tarde.", getStepName());
                }

                context.put("disponibilidade", disponibilidade);
                context.put("ultimoDia", nextEndDate.toString());
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

            Disponibilidade chosenDay = disponibilidade.get(choice);
            context.put("diaEscolhido", choice);

            List<LocalTime> availableTimes = chosenDay.getHorarios();
            chat.setDadosContexto(objectMapper.writeValueAsString(context));

            StringBuilder response = new StringBuilder("Perfeito! Agora, escolha um horário:\n");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            for (int i = 0; i < availableTimes.size(); i++) {
                response.append(String.format("%d - %s\n", i + 1, availableTimes.get(i).format(timeFormatter)));
            }
            response.append(String.format("%d - Escolher outro dia", availableTimes.size() + 1));

            return new StepResponse(response.toString(), "AGUARDANDO_ESCOLHA_HORARIO");

        } catch (Exception e) {
            logger.error("FALHA: Erro ao processar escolha de dia", e);
            return new StepResponse("Ocorreu um erro ao processar sua escolha. Tente novamente.", getStepName());
        }
    }
}
