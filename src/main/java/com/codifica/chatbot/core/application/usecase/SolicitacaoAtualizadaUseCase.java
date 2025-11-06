package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoAtualizadaEventListenerPort;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAtualizadaEvent;
import com.codifica.chatbot.infrastructure.chat.ChatFlowService;
import com.codifica.chatbot.interfaces.adapters.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

public class SolicitacaoAtualizadaUseCase implements SolicitacaoAtualizadaEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoAtualizadaUseCase.class);

    private final UpdateChatUseCase updateChatUseCase;
    private final FindChatByIdUseCase findChatByIdUseCase;
    private final ChatFlowService chatFlowService;
    private final Optional<NotificationService> notificationService;

    private final ObjectMapper objectMapper;

    public SolicitacaoAtualizadaUseCase(UpdateChatUseCase updateChatUseCase,
                                        ObjectMapper objectMapper,
                                        FindChatByIdUseCase findChatByIdUseCase,
                                        ChatFlowService chatFlowService,
                                        Optional<NotificationService> notificationService) {
        this.updateChatUseCase = updateChatUseCase;
        this.objectMapper = objectMapper;
        this.findChatByIdUseCase = findChatByIdUseCase;
        this.chatFlowService = chatFlowService;
        this.notificationService = notificationService;
    }

    @Override
    public void processSolicitacaoAtualizada(SolicitacaoAtualizadaEvent event) {
        Long chatId = event.getSolicitacao().getChatId();

        try {
            String dadosContexto = objectMapper.writeValueAsString(event);
            Chat chat = new Chat(chatId, "SOLICITACAO_RESPONDIDA_PETSHOP", dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);

            if  (event.getAceito()) {
                logger.info("ATUALIZAÇÃO: Solicitação {} do Chat {} aceita pelo usuário.", event.getSolicitacao().getId(), chatId);
            }
            else {
                logger.info("ATUALIZAÇÃO: Solicitação {} do Chat {} recusada pelo usuário.", event.getSolicitacao().getId(), chatId);
            }

            if (notificationService.isPresent()) {
                Chat updatedChat = findChatByIdUseCase.execute(chatId).orElseThrow();
                String responseMessage = chatFlowService.processMessage(updatedChat, "");
                notificationService.get().sendMessage(chatId, responseMessage);
            }
        } catch (JsonProcessingException e) {
            logger.error("Erro ao serializar SolicitacaoAtualizadaEvent para o chat {}", chatId, e);
        }
    }
}
