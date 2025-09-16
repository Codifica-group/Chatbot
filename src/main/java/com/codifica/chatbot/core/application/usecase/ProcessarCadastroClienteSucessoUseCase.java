package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.ProcessarCadastroClienteSucessoUseCasePort;
import com.codifica.chatbot.core.domain.chat.ChatRepository;
import com.codifica.chatbot.infrastructure.persistence.chat_cliente.ChatClienteEntity;
import com.codifica.chatbot.infrastructure.persistence.chat_cliente.ChatClienteJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class ProcessarCadastroClienteSucessoUseCase implements ProcessarCadastroClienteSucessoUseCasePort {

    private static final Logger logger = LoggerFactory.getLogger(ProcessarCadastroClienteSucessoUseCase.class);
    private final ChatRepository chatRepository;
    private final ChatClienteJpaRepository chatClienteJpaRepository;

    public ProcessarCadastroClienteSucessoUseCase(ChatRepository chatRepository, ChatClienteJpaRepository chatClienteJpaRepository) {
        this.chatRepository = chatRepository;
        this.chatClienteJpaRepository = chatClienteJpaRepository;
    }

    @Override
    public void processar(Integer chatId, Integer clienteId) {
        chatRepository.findById(chatId).ifPresentOrElse(
                chat -> {
                    chat.setPassoAtual("AGUARDANDO_CADASTRO_PET");
                    chat.setDataAtualizacao(LocalDateTime.now());
                    chatRepository.save(chat);

                    ChatClienteEntity associacao = new ChatClienteEntity(chatId, clienteId);
                    chatClienteJpaRepository.save(associacao);

                    logger.info("Chat {} atualizado para o passo {} e associado ao clienteId {}.", chatId, chat.getPassoAtual(), clienteId);
                },
                () -> logger.error("Não foi possível encontrar o chat com ID {} para processar o sucesso do cadastro.", chatId)
        );
    }
}
