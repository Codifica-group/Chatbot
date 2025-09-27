package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.shared.Dia;
import com.codifica.chatbot.infrastructure.services.MainBackendService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ListAvailableTimesStepHandler implements ConversationStep {

    private final MainBackendService mainBackendService;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
            List<Dia> availableDays = (List<Dia>) context.get("availableDays");
            int choice = Integer.parseInt(userMessage) - 1;

            if (choice == availableDays.size()) {
                LocalDate lastDate = LocalDate.parse((String) context.get("lastDate"));
                availableDays = mainBackendService.getAvailableDays(lastDate.plusDays(1), lastDate.plusDays(15));
                context.put("availableDays", availableDays);
                context.put("lastDate", lastDate.plusDays(15));
                chat.setDadosContexto(objectMapper.writeValueAsString(context));

                StringBuilder response = new StringBuilder("Sem problemas! Aqui estão mais alguns dias disponíveis:\n");
                for (int i = 0; i < availableDays.size(); i++) {
                    response.append(String.format("%d - %s - %s\n", i + 1, availableDays.get(i).getData(), availableDays.get(i).getDiaSemana()));
                }
                response.append(String.format("%d - Ver mais datas", availableDays.size() + 1));

                return new StepResponse(response.toString(), "AGUARDANDO_ESCOLHA_DIA");
            }

            if (choice < 0 || choice > availableDays.size()) {
                return new StepResponse("Opção inválida, por favor, tente novamente.", getStepName());
            }

            Dia chosenDay = availableDays.get(choice);
            context.put("chosenDay", chosenDay);
            chat.setDadosContexto(objectMapper.writeValueAsString(context));
            List<String> availableTimes = mainBackendService.getAvailableTimes(chosenDay.getData());

            StringBuilder response = new StringBuilder("Perfeito! Agora, escolha um horário:\n");
            for (int i = 0; i < availableTimes.size(); i++) {
                response.append(String.format("%d - %s\n", i + 1, availableTimes.get(i)));
            }
            response.append(String.format("%d - Escolher outro dia", availableTimes.size() + 1));


            return new StepResponse(response.toString(), "AGUARDANDO_ESCOLHA_HORARIO");

        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar sua escolha. Tente novamente.", getStepName());
        }
    }
}
