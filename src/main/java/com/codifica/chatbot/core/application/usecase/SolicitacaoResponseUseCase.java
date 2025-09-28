package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoEventListenerPort;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAtualizadaEvent;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class SolicitacaoResponseUseCase implements SolicitacaoEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoResponseUseCase.class);
    private final UpdateChatUseCase updateChatUseCase;

    public SolicitacaoResponseUseCase(UpdateChatUseCase updateChatUseCase) {
        this.updateChatUseCase = updateChatUseCase;
    }

    @Override
    public void processSolicitacaoParaCadastrarResponse(SolicitacaoParaCadastrarResponseEvent event) {
        if (event.getStatus() == StatusEvent.SUCESSO) {
            logger.info("SUCESSO: Solicitacao do chatId {} cadastrada com Id: {}", event.getChatId(), event.getSolicitacaoId());
            String passoAtual = "AGUARDANDO_ORÇAMENTO";
            String dadosContexto = "{\"solicitacao\": \"" + event.getSolicitacaoId() + "\"}";
            Chat chat = new Chat(event.getChatId(), passoAtual, dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);
            logger.info("ATUALIZAÇÃO: Chat {}, Solicitacao {}, Passo atual {}.", event.getChatId(), event.getSolicitacaoId(), passoAtual);
        } else {
            logger.error("FALHA: Erro ao cadastrar solicitacao do chatId {}: {}", event.getChatId(), event.getErro());
            String passoAtual = "ERRO_CADASTRO_SOLICITACAO";
            String dadosContexto = "{\"erro\": \"" + event.getErro() + "\"}";
            Chat chat = new Chat(event.getChatId(), passoAtual, dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);
        }
    }

    @Override
    public void processSolicitacaoAtualizada(SolicitacaoAtualizadaEvent event) {

        // TODO: Mover esse método para SolcitacaoParaCadastrarResponseUseCase

        if ("AGUARDANDO_CONFIRMACAO_CLIENTE".equals(event.getStatus().toString())) {
            logger.info("ATUALIZAÇÂO: Solicitacao {} do chatId {} aprovada.", event.getSolicitacao().getId(), event.getSolicitacao().getChatId());
            String passoAtual = "AGUARDANDO_CONFIRMACAO_CLIENTE";
            String dadosContexto = "{\"solicitacao\": \"" + event.getSolicitacao() + "\"dataInicioAlterada\": \"" + event.isDataInicioAlterada() + "\"}";
            Chat chat = new Chat(event.getSolicitacao().getChatId(), passoAtual, dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);
//            String message = "Sua solicitação foi atualizada: " + event.toString() + " Precisamos da sua apovação, digite: 1 - Confirmar Agendamento, 2 - Cancelar Agendamento";
//            updateChatAndNotify(event.getSolicitacao().getChatId(), "AGUARDANDO_CONFIRMACAO_CLIENTE", message, event.getSolicitacao().getId());
        } else if ("CANCELADO_PELO_USUARIO".equals(event.getStatus().toString())) {
            String message = "Infelizmente sua solicitação de agendamento foi recusada, tente agendar em outra data.";
            updateChatAndNotify(event.getSolicitacao().getChatId(), "AGUARDANDO_AGENDAMENTO", message, event.getSolicitacao().getId());
        }
    }

    private void updateChatAndNotify(Integer chatId, String nextStep, String message, Integer solicitacaoId) {
        String dadosContexto = "{\"solicitacaoId\": " + solicitacaoId + "}";
        Chat chat = new Chat(chatId, nextStep, dadosContexto, LocalDateTime.now(), null);
        updateChatUseCase.updateChatStatus(chat);
        // Here you would typically send the message to the user through the appropriate adapter (e.g., Telegram, Terminal)
        System.out.println("Bot: " + message);
    }
}
