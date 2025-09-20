package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.ClienteEventListenerPort;
import com.codifica.chatbot.core.domain.chat.ChatRepository;
import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
import com.codifica.chatbot.infrastructure.persistence.chat_cliente.ChatClienteEntity;
import com.codifica.chatbot.infrastructure.persistence.chat_cliente.ChatClienteJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class ClienteParaCadastrarResponseUseCase implements ClienteEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(ClienteParaCadastrarResponseUseCase.class);
    private final ChatRepository chatRepository;
    private final ChatClienteJpaRepository chatClienteJpaRepository;

    public ClienteParaCadastrarResponseUseCase(ChatRepository chatRepository, ChatClienteJpaRepository chatClienteJpaRepository) {
        this.chatRepository = chatRepository;
        this.chatClienteJpaRepository = chatClienteJpaRepository;
    }

    @Override
    public void processClienteParaCadastrarResponse(ClienteParaCadastrarResponseEvent event) {
        if (event.getStatus() == StatusEvent.SUCESSO) {
            handleSuccess(event.getChatId(), event.getClienteId());
        } else {
            handleFailure(event.getChatId(), event.getErro());
        }
    }

    private void handleSuccess(Integer chatId, Integer clienteId) {
        logger.info("SUCESSO: Cliente do chatId {} cadastrado com Id: {}", chatId, clienteId);
        chatRepository.findById(chatId).ifPresentOrElse(
                chat -> {
                    chat.setPassoAtual("AGUARDANDO_CADASTRO_PET");
                    chat.setDataAtualizacao(LocalDateTime.now());
                    chatRepository.save(chat);

                    ChatClienteEntity associacao = new ChatClienteEntity(chatId, clienteId);
                    chatClienteJpaRepository.save(associacao);

                    logger.info("ATUALIZAÇÃO: Chat {}, Cliente {}, Passo atual {}.", chatId, clienteId, chat.getPassoAtual());
                },
                () -> logger.error("FALHA: Chat {} não encontrado.", chatId)
        );
    }

    private void handleFailure(Integer chatId, String erro) {
        logger.error("FALHA: Erro ao cadastrar cliente do chatId {}: {}", chatId, erro);
        // TODO: adicionar a lógica para reverter o passo do chat ou notificar o usuário
        chatRepository.findById(chatId).ifPresent(chat -> {
            chat.setPassoAtual("ERRO_CADASTRO_CLIENTE");
            chatRepository.save(chat);
        });
    }
}
