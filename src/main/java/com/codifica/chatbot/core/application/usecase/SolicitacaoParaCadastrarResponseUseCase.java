package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoEventListenerPort;
import com.codifica.chatbot.core.domain.chat.ChatRepository;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class SolicitacaoParaCadastrarResponseUseCase implements SolicitacaoEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoParaCadastrarResponseUseCase.class);
    private final ChatRepository chatRepository;

    public SolicitacaoParaCadastrarResponseUseCase(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void processSolicitacaoParaCadastrarResponse(SolicitacaoParaCadastrarResponseEvent event) {
        if (event.getStatus() == StatusEvent.SUCESSO) {
            handleSuccess(event.getChatId(), event.getSolicitacaoId());
        } else {
            handleFailure(event.getChatId(), event.getErro());
        }
    }

    private void handleSuccess(Integer chatId, Integer solicitacaoId) {
        logger.info("SUCESSO: Solicitacao do chatId {} cadastrada com Id: {}", chatId, solicitacaoId);
        chatRepository.findById(chatId).ifPresentOrElse(
                chat -> {
                    chat.setPassoAtual("AGENDAMENTO_REALIZADO");
                    chat.setDataAtualizacao(LocalDateTime.now());
                    chatRepository.save(chat);

                    logger.info("ATUALIZAÇÃO: Chat {}, Solicitacao {}, Passo atual {}.", chatId, solicitacaoId, chat.getPassoAtual());
                },
                () -> logger.error("FALHA: Chat {} não encontrado.", chatId)
        );
    }

    private void handleFailure(Integer chatId, String erro) {
        logger.error("FALHA: Erro ao cadastrar solicitacao do chatId {}: {}", chatId, erro);
        chatRepository.findById(chatId).ifPresent(chat -> {
            chat.setPassoAtual("ERRO_CADASTRO_SOLICITACAO");
            chatRepository.save(chat);
        });
    }
}
