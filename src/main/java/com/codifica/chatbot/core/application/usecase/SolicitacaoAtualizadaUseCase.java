package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoAtualizadaEventListenerPort;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAtualizadaEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class SolicitacaoAtualizadaUseCase implements SolicitacaoAtualizadaEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoAtualizadaUseCase.class);

    private final UpdateChatUseCase updateChatUseCase;

    private final ObjectMapper objectMapper;

    public SolicitacaoAtualizadaUseCase(UpdateChatUseCase updateChatUseCase, ObjectMapper objectMapper) {
        this.updateChatUseCase = updateChatUseCase;
        this.objectMapper = objectMapper;
    }

    @Override
    public void processSolicitacaoAtualizada(SolicitacaoAtualizadaEvent event) {
        if (event.getAceito()) {
            try {
                String dadosContexto = objectMapper.writeValueAsString(event);
                Chat chat = new Chat(event.getSolicitacao().getChatId(), "ACEITO_PELO_USUARIO", dadosContexto, LocalDateTime.now(), null);
                updateChatUseCase.updateChatStatus(chat);
                logger.info("ATUALIZAÇÃO: Solicitação {} do Chat {} aceita pelo usuário.", event.getSolicitacao().getId(), event.getSolicitacao().getChatId());
            } catch (JsonProcessingException e) {
                logger.error("Erro ao serializar SolicitacaoAtualizadaEvent para o chat {}", event.getSolicitacao().getChatId(), e);
            }
        } else {
            logger.info("ATUALIZAÇÃO: Solicitação {} do Chat {} recusada pelo usuário.", event.getSolicitacao().getId(), event.getSolicitacao().getChatId());
        }
    }
}
