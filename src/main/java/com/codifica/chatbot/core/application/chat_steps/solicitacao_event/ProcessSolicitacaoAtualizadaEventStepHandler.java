package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.application.ports.out.SolicitacaoEventPublisherPort;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAtualizadaEvent;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaAtualizarEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ProcessSolicitacaoAtualizadaEventStepHandler implements ConversationStep {

    private final SolicitacaoEventPublisherPort solicitacaoEventPublisherPort;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProcessSolicitacaoAtualizadaEventStepHandler(SolicitacaoEventPublisherPort solicitacaoEventPublisherPort) {
        this.solicitacaoEventPublisherPort = solicitacaoEventPublisherPort;
    }

    @Override
    public String getStepName() {
        return "ACEITO_PELO_USUARIO";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, Object> context = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            SolicitacaoAtualizadaEvent solicitacao = objectMapper.readValue(context.get("solicitacao").toString(), SolicitacaoAtualizadaEvent.class);

            // TODO: Mensagem personalizada para dataInicioAlterada
            boolean dataInicioAlterada = (boolean) context.get("dataInicioAlterada");

            String message = "Sua solicitação foi atualizada: " + solicitacao.toString() + " Precisamos da sua apovação, digite: 1 - Confirmar Agendamento, 2 - Cancelar Agendamento";
            return new StepResponse(message, "AGUARDANDO_CONFIRMACAO_CLIENTE");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar a atualização da solicitação.", getStepName());
        }
    }
}
