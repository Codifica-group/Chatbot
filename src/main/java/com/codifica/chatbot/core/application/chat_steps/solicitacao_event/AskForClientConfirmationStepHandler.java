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

public class AskForClientConfirmationStepHandler implements ConversationStep {

    private final SolicitacaoEventPublisherPort solicitacaoEventPublisherPort;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AskForClientConfirmationStepHandler(SolicitacaoEventPublisherPort solicitacaoEventPublisherPort) {
        this.solicitacaoEventPublisherPort = solicitacaoEventPublisherPort;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_CONFIRMACAO_CLIENTE";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, Object> context = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            SolicitacaoAtualizadaEvent solicitacao = objectMapper.readValue(context.get("solicitacao").toString(), SolicitacaoAtualizadaEvent.class);
            String status;

            if ("1".equals(userMessage.trim())) {
                status = "CONFIRMADO_PELO_CLIENTE";
            } else if ("2".equals(userMessage.trim())) {
                status = "CANCELADO_PELO_CLIENTE";
            } else {
                return new StepResponse("Opção inválida. Por favor, digite 1 para confirmar ou 2 para cancelar.", getStepName());
            }

            SolicitacaoParaAtualizarEvent event = new SolicitacaoParaAtualizarEvent(chat.getId(), solicitacao.getSolicitacao().getId(), status);
            solicitacaoEventPublisherPort.publishSolicitacaoParaAtualizar(event);

            return new StepResponse("Obrigado pela sua resposta. Sua solicitação foi atualizada.", "AGUARDANDO_AGENDAMENTO");

        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar sua confirmação. Tente novamente.", getStepName());
        }
    }
}
