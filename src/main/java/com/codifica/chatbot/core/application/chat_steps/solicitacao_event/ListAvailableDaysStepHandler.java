package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.pet.Pet;
import com.codifica.chatbot.core.domain.shared.Dia;
import com.codifica.chatbot.infrastructure.services.MainBackendService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ListAvailableDaysStepHandler implements ConversationStep {

    private static final Logger logger = LoggerFactory.getLogger(ListAvailableDaysStepHandler.class);
    private final MainBackendService mainBackendService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public ListAvailableDaysStepHandler(MainBackendService mainBackendService) {
        this.mainBackendService = mainBackendService;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_ESCOLHA_PET";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, Object> context = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            List<Pet> pets = objectMapper.convertValue(context.get("pets"), new TypeReference<List<Pet>>() {});

            String validationError = ValidationUtil.validateIntegerChoice(userMessage, pets.size() + 1);
            if (validationError != null) {
                return new StepResponse(validationError, getStepName());
            }

            int choice = Integer.parseInt(userMessage) - 1;

            if (choice == pets.size()) {
                chat.setDadosContexto("{}");
                return new StepResponse("Ok, vamos cadastrar um novo pet. Qual o nome dele?", "AGUARDANDO_NOME_PET");
            }

            context.put("petId", pets.get(choice).getId());
            context.remove("pets");

            LocalDate startDate = LocalDate.now();
            List<Dia> availableDays = mainBackendService.getAvailableDays(startDate, startDate.plusDays(14));
            context.put("availableDays", availableDays);
            context.put("lastDate", startDate.plusDays(14).toString());
            chat.setDadosContexto(objectMapper.writeValueAsString(context));

            StringBuilder response = new StringBuilder("Ótimo! Aqui estão os dias disponíveis para agendamento:\n");
            for (int i = 0; i < availableDays.size(); i++) {
                response.append(String.format("%d - %s - %s\n", i + 1, availableDays.get(i).getData(), availableDays.get(i).getDiaSemana()));
            }
            response.append(String.format("%d - Ver mais datas", availableDays.size() + 1));

            return new StepResponse(response.toString(), "AGUARDANDO_ESCOLHA_DIA");

        } catch (Exception e) {
            logger.error("FALHA: Erro ao processar escolha do pet:", e);
            return new StepResponse("Ocorreu um erro ao processar sua escolha. Tente novamente.", getStepName());
        }
    }
}
