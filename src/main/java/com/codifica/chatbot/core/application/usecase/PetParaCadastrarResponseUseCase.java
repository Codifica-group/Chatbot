package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.PetEventListenerPort;
import com.codifica.chatbot.core.domain.chat.ChatRepository;
import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class PetParaCadastrarResponseUseCase implements PetEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(PetParaCadastrarResponseUseCase.class);
    private final ChatRepository chatRepository;

    public PetParaCadastrarResponseUseCase(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void processPetParaCadastrarResponse(PetParaCadastrarResponseEvent event) {
        if (event.getStatus() == StatusEvent.SUCESSO) {
            handleSuccess(event.getChatId(), event.getClienteId(), event.getPetId());
        } else {
            handleFailure(event.getChatId(), event.getErro());
        }
    }

    private void handleSuccess(Integer chatId, Integer ClienteId, Integer petId) {
        logger.info("SUCESSO: Pet do chatId {} cadastrado com Id: {}", chatId, petId);
        chatRepository.findById(chatId).ifPresentOrElse(
                chat -> {
                    chat.setPassoAtual("AGUARDANDO_AGENDAMENTO");
                    chat.setDataAtualizacao(LocalDateTime.now());
                    chatRepository.save(chat);

                    logger.info("ATUALIZAÇÃO: Chat {}, Cliente {}, Pet {}, Passo atual {}.", chatId, ClienteId, petId, chat.getPassoAtual());
                },
                () -> logger.error("FALHA: Chat {} não encontrado.", chatId)
        );
    }

    private void handleFailure(Integer chatId, String erro) {
        logger.error("FALHA: Erro ao cadastrar pet do chatId {}: {}", chatId, erro);
        chatRepository.findById(chatId).ifPresent(chat -> {
            chat.setPassoAtual("ERRO_CADASTRO_PET");
            chatRepository.save(chat);
        });
    }
}
