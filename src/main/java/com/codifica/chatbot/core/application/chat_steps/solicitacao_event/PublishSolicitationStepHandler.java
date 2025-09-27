package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.application.ports.out.SolicitacaoEventPublisherPort;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PublishSolicitationStepHandler implements ConversationStep {

    private final SolicitacaoEventPublisherPort solicitacaoEventPublisherPort;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PublishSolicitationStepHandler(SolicitacaoEventPublisherPort solicitacaoEventPublisherPort) {
        this.solicitacaoEventPublisherPort = solicitacaoEventPublisherPort;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_ESCOLHA_SERVICOS";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, Object> context = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            List<Integer> servicosIds = Arrays.stream(userMessage.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            context.put("servicos", servicosIds);
            chat.setDadosContexto(objectMapper.writeValueAsString(context));

            Integer petId = (Integer) context.get("petId");
            String chosenDay = (String) context.get("chosenDay");
            String chosenTime = (String) context.get("chosenTime");

            SolicitacaoParaCadastrarEvent event = new SolicitacaoParaCadastrarEvent(
                    chat.getId(),
                    petId,
                    servicosIds,
                    LocalDate.parse(chosenDay, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(LocalTime.parse(chosenTime)),
                    null,
                    "PENDENTE"
            );

            solicitacaoEventPublisherPort.publishSolicitacaoParaCadastrar(event);

            return new StepResponse("Sua solicitação foi enviada com sucesso! Em breve você receberá a confirmação.", "AGUARDANDO_CONFIRMACAO_AGENDAMENTO");

        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar os serviços. Tente novamente.", getStepName());
        }
    }
}
