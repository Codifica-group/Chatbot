package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoAceitaResponseEventListenerPort;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAceitaResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

public class SolicitacaoAceitaResponseUseCase implements SolicitacaoAceitaResponseEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoAceitaResponseUseCase.class);
    private final FindChatByIdUseCase findChatByIdUseCase;
    private final UpdateChatUseCase updateChatUseCase;

    public SolicitacaoAceitaResponseUseCase(FindChatByIdUseCase findChatByIdUseCase, UpdateChatUseCase updateChatUseCase) {
        this.findChatByIdUseCase = findChatByIdUseCase;
        this.updateChatUseCase = updateChatUseCase;
    }

    @Override
    public void processaSolicitacaoAceitaResponse(SolicitacaoAceitaResponseEvent event) {
        Chat chat = findChatByIdUseCase.execute(event.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat não encontrado para o ID: " + event.getChatId()));

        if (event.getStatus() == StatusEvent.SUCESSO) {
            logger.info("SUCESSO: Resposta de solicitação aceita para o chatId {}", event.getChatId());
            chat.setPassoAtual("AGENDAMENTO_CONFIRMADO");
        } else {
            logger.error("FALHA: Erro ao processar resposta de solicitação aceita para o chatId {}: {}", event.getChatId(), event.getErro());
            chat.setDadosContexto("{\"erro\": \"" + event.getErro() + "\"}");
            chat.setPassoAtual("ERRO");
        }

        chat.setDataAtualizacao(LocalDateTime.now());
        updateChatUseCase.updateChatStatus(chat);
    }
}
