package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.shared.DiaDaSemana;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ResponseForSolicitationAcceptStepHandler implements ConversationStep {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    @Override
    public String getStepName() {
        return "SOLICITACAO_FINALIZADA";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, Object> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            boolean aceito = (Boolean) dadosContexto.get("aceito");

            String responseMessage;
            if (aceito) {
                Map<String, Object> solicitacao = (Map<String, Object>) dadosContexto.get("solicitacao");
                LocalDateTime dataHoraInicio = LocalDateTime.parse((String) solicitacao.get("dataHoraInicio"));
                String diaDaSemana = DiaDaSemana.fromDate(dataHoraInicio.toLocalDate());
                String dataFormatada = diaDaSemana + ", " + dataHoraInicio.format(formatter);

                responseMessage = "Tudo certo! Seu agendamento foi confirmado para " + dataFormatada + ". Estamos ansiosos para recebê-lo!";
            } else {
                responseMessage = "Agendamento cancelado. Se mudar de ideia, é só chamar!";
            }
            chat.setDadosContexto("{}");
            return new StepResponse(responseMessage, "IDLE");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao exibir a mensagem final." + e, "IDLE");
        }
    }
}
