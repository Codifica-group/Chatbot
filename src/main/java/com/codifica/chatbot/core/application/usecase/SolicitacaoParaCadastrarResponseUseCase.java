package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoEventListenerPort;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class SolicitacaoParaCadastrarResponseUseCase implements SolicitacaoEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoParaCadastrarResponseUseCase.class);
    private final UpdateChatUseCase updateChatUseCase;

    public SolicitacaoParaCadastrarResponseUseCase(UpdateChatUseCase updateChatUseCase) {
        this.updateChatUseCase = updateChatUseCase;
    }

    @Override
    public void processSolicitacaoParaCadastrarResponse(SolicitacaoParaCadastrarResponseEvent event) {
        if (event.getStatus() == StatusEvent.SUCESSO) {
            logger.info("SUCESSO: Solicitacao do chatId {} cadastrada com Id: {}", event.getChatId(), event.getSolicitacaoId());
            String passoAtual = "AGUARDANDO_RESPOSTA_SOLICITACAO_PETSHOP";
            String dadosContexto = "{\"solicitacao\": \"" + event.getSolicitacaoId() + "\"}";
            Chat chat = new Chat(event.getChatId(), passoAtual, dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);
            logger.info("ATUALIZAÇÃO: Chat {}, Solicitacao {}, Passo atual {}.", event.getChatId(), event.getSolicitacaoId(), passoAtual);
        } else {
            logger.error("FALHA: Erro ao cadastrar solicitacao do chatId {}: {}", event.getChatId(), event.getErro());
//            String passoAtual = "ERRO_CADASTRO_SOLICITACAO";
            String passoAtual = "ERRO";
            String dadosContexto = "{\"erro\": \"" + event.getErro() + "\"}";
            Chat chat = new Chat(event.getChatId(), passoAtual, dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);
        }
    }
}
