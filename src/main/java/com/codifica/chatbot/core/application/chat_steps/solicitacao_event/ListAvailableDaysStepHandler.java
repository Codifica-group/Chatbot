package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.agenda.Agenda;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.disponibilidade.Disponibilidade;
import com.codifica.chatbot.core.domain.pet.Pet;
import com.codifica.chatbot.core.domain.shared.DiaDaSemana;
import com.codifica.chatbot.infrastructure.services.MainBackendService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ListAvailableDaysStepHandler implements ConversationStep {

    private static final Logger logger = LoggerFactory.getLogger(ListAvailableDaysStepHandler.class);
    private final MainBackendService mainBackendService;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

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

            Pet chosenPet = pets.get(choice);

            Optional<Agenda> futureAgendaOpt = mainBackendService.getFutureAgendaByPetId(chosenPet.getId());
            if (futureAgendaOpt.isPresent()) {
                Agenda nextAgenda = futureAgendaOpt.get();
                String diaDaSemana = DiaDaSemana.fromDate(nextAgenda.getDataHoraInicio().toLocalDate());
                String dataFormatada = diaDaSemana + ", " + nextAgenda.getDataHoraInicio().format(formatter);
                String errorMessage = String.format(
                        "Ops! %s já tem um agendamento marcado para %s 📅 Não podemos marcar outro por enquanto",
                        chosenPet.getNome(),
                        dataFormatada
                );
                chat.setDadosContexto("{}");
                return new StepResponse(errorMessage, "IDLE");
            }

            context.put("petId", chosenPet.getId());
            context.remove("pets");

            LocalDateTime startDate = LocalDateTime.now();
            List<Disponibilidade> disponibilidade = mainBackendService.getDisponibilidade(startDate, startDate.plusDays(14));
            context.put("disponibilidade", disponibilidade);
            context.put("ultimoDia", startDate.plusDays(14).toString());
            chat.setDadosContexto(objectMapper.writeValueAsString(context));

            if (disponibilidade.isEmpty()) { // TODO: tratar caso nenhum dia disponivel em 14 dias
                chat.setDadosContexto("{}");
                return new StepResponse("Desculpe, não encontrei horários disponíveis nos próximos 14 dias. Por favor, tente novamente mais tarde.", "IDLE");
            }

            StringBuilder response = new StringBuilder("Ótimo! Aqui estão os próximos dias com horários disponíveis 📅\n");
            for (int i = 0; i < disponibilidade.size(); i++) {
                response.append(String.format("%d - %s - %s\n",
                        i + 1,
                        disponibilidade.get(i).getDia().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        disponibilidade.get(i).getDiaSemana()));
            }
            response.append(String.format("%d - Ver mais datas", disponibilidade.size() + 1));

            return new StepResponse(response.toString(), "AGUARDANDO_ESCOLHA_DIA");

        } catch (Exception e) {
            logger.error("FALHA: Erro ao processar escolha do pet:", e);
            return new StepResponse("Ocorreu um erro ao processar sua escolha. Tente novamente.", getStepName());
        }
    }
}
