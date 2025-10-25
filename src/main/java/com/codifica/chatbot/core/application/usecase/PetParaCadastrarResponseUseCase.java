package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.PetEventListenerPort;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class PetParaCadastrarResponseUseCase implements PetEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(PetParaCadastrarResponseUseCase.class);
    private final UpdateChatUseCase updateChatUseCase;

    public PetParaCadastrarResponseUseCase(UpdateChatUseCase updateChatUseCase) {
        this.updateChatUseCase = updateChatUseCase;
    }

    @Override
    public void processPetParaCadastrarResponse(PetParaCadastrarResponseEvent event) {
        if (event.getStatus() == StatusEvent.SUCESSO) {
            logger.info("SUCESSO: Pet do chatId {} cadastrado com Id: {}", event.getChatId(), event.getPetId());
            String passoAtual = "AGUARDANDO_AGENDAMENTO";
            String dadosContexto = "{}";
            Chat chat = new Chat(event.getChatId(), passoAtual, dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);
            logger.info("ATUALIZAÇÃO: Chat {}, Cliente {}, Pet {}, Passo atual {}.", event.getChatId(), event.getClienteId(), event.getPetId(), passoAtual);
        } else {
            logger.error("FALHA: Erro ao cadastrar pet do chatId {}: {}", event.getChatId(), event.getErro());
//            String passoAtual = "ERRO_CADASTRO_PET";
            String passoAtual = "ERRO";
            String dadosContexto = "{\"erro\": \"" + event.getErro() + "\"}";
            Chat chat = new Chat(event.getChatId(), passoAtual, dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);
        }
    }
}
