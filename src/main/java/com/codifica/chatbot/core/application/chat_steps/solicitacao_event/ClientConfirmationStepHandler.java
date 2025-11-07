package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.application.ports.out.SolicitacaoAceitaEventPublisherPort;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAceitaEvent;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAtualizadaEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.Map;


public class ClientConfirmationStepHandler implements ConversationStep {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final SolicitacaoAceitaEventPublisherPort solicitacaoAceitaEventPublisherPort;
    private final UpdateChatUseCase updateChatUseCase;

    public ClientConfirmationStepHandler(SolicitacaoAceitaEventPublisherPort solicitacaoAceitaEventPublisherPort, UpdateChatUseCase updateChatUseCase) {
        this.solicitacaoAceitaEventPublisherPort = solicitacaoAceitaEventPublisherPort;
        this.updateChatUseCase = updateChatUseCase;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_RESPOSTA_SOLICITACAO_CLIENTE";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        String validationError = ValidationUtil.validateIntegerChoice(userMessage, 2);
        if (validationError != null) {
            return new StepResponse(validationError, getStepName());
        }

        try {
            Map<String, Object> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            SolicitacaoAtualizadaEvent event = objectMapper.readValue(chat.getDadosContexto(), SolicitacaoAtualizadaEvent.class);
            boolean aceito = "1".equals(userMessage.trim());

            solicitacaoAceitaEventPublisherPort.publishSolicitacaoAceita(
                    new SolicitacaoAceitaEvent(chat.getId(), event.getSolicitacao().getId(), LocalDateTime.now(), aceito)
            );

            dadosContexto.put("aceito", aceito);
            chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));
            chat.setDataAtualizacao(LocalDateTime.now());
            updateChatUseCase.updateChatStatus(chat);

            String responseMessage;
            if (aceito) {
                responseMessage = "Ótimo! Estou confirmando seu agendamento, só um momento por favor... ⌛";
            } else {
                responseMessage = "Entendido! Estou cancelando sua solicitação, só um momento... ⌛";
            }

            return new StepResponse(responseMessage, "AGUARDANDO_RESPOSTA_SOLICITACAO_API");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar sua confirmação. Tente novamente.", getStepName());
        }
    }
}
