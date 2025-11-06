package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoAceitaResponseEventListenerPort;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAceitaResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;

import com.codifica.chatbot.infrastructure.chat.ChatFlowService;
import com.codifica.chatbot.interfaces.adapters.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.Optional;

public class SolicitacaoAceitaResponseUseCase implements SolicitacaoAceitaResponseEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoAceitaResponseUseCase.class);
    private final FindChatByIdUseCase findChatByIdUseCase;
    private final UpdateChatUseCase updateChatUseCase;
    private final ChatFlowService chatFlowService;
    private final Optional<NotificationService> notificationService;

    public SolicitacaoAceitaResponseUseCase(FindChatByIdUseCase findChatByIdUseCase,
                                            UpdateChatUseCase updateChatUseCase,
                                            ChatFlowService chatFlowService,
                                            Optional<NotificationService> notificationService) {
        this.findChatByIdUseCase = findChatByIdUseCase;
        this.updateChatUseCase = updateChatUseCase;
        this.chatFlowService = chatFlowService;
        this.notificationService = notificationService;
    }

    @Override
    public void processaSolicitacaoAceitaResponse(SolicitacaoAceitaResponseEvent event) {
        Chat chat = findChatByIdUseCase.execute(event.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat não encontrado para o ID: " + event.getChatId()));

        if (event.getStatus() == StatusEvent.SUCESSO) {
            logger.info("SUCESSO: Resposta de solicitação aceita para o chatId {}", event.getChatId());
            chat.setPassoAtual("SOLICITACAO_FINALIZADA");
        } else {
            logger.error("FALHA: Erro ao processar resposta de solicitação aceita para o chatId {}: {}", event.getChatId(), event.getErro());
            chat.setDadosContexto("{\"erro\": \"" + event.getErro() + "\"}");
            chat.setPassoAtual("ERRO");
        }

        chat.setDataAtualizacao(LocalDateTime.now());
        updateChatUseCase.updateChatStatus(chat);

        if (notificationService.isPresent()) {
            String responseMessage = chatFlowService.processMessage(chat, "");
            notificationService.get().sendMessage(chat.getId(), responseMessage);
        }
    }
}
