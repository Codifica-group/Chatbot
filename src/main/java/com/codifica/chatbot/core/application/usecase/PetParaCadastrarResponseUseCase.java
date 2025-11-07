package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.PetEventListenerPort;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
import com.codifica.chatbot.infrastructure.chat.ChatFlowService;
import com.codifica.chatbot.interfaces.adapters.telegram.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

public class PetParaCadastrarResponseUseCase implements PetEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(PetParaCadastrarResponseUseCase.class);
    private final UpdateChatUseCase updateChatUseCase;
    private final FindChatByIdUseCase findChatByIdUseCase;
    private final ChatFlowService chatFlowService;
    private final Optional<NotificationService> notificationService;

    public PetParaCadastrarResponseUseCase(UpdateChatUseCase updateChatUseCase,
                                           FindChatByIdUseCase findChatByIdUseCase,
                                           ChatFlowService chatFlowService,
                                           Optional<NotificationService> notificationService) {
        this.updateChatUseCase = updateChatUseCase;
        this.findChatByIdUseCase = findChatByIdUseCase;
        this.chatFlowService = chatFlowService;
        this.notificationService = notificationService;
    }

    @Override
    public void processPetParaCadastrarResponse(PetParaCadastrarResponseEvent event) {
        Chat chat;

        if (event.getStatus() == StatusEvent.SUCESSO) {
            logger.info("SUCESSO: Pet do chatId {} cadastrado com Id: {}", event.getChatId(), event.getPetId());
            String passoAtual = "AGUARDANDO_AGENDAMENTO";
            String dadosContexto = "{}";
            chat = new Chat(event.getChatId(), passoAtual, dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);
            logger.info("ATUALIZAÇÃO: Chat {}, Cliente {}, Pet {}, Passo atual {}.", event.getChatId(), event.getClienteId(), event.getPetId(), passoAtual);
        } else {
            logger.error("FALHA: Erro ao cadastrar pet do chatId {}: {}", event.getChatId(), event.getErro());
//            String passoAtual = "ERRO_CADASTRO_PET";
            String passoAtual = "ERRO";
            String dadosContexto = "{\"erro\": \"" + event.getErro() + "\"}";
            chat = new Chat(event.getChatId(), passoAtual, dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);
        }

        if (notificationService.isPresent()) {
            Chat updatedChat = findChatByIdUseCase.execute(chat.getId()).orElse(chat);
            String responseMessage = chatFlowService.processMessage(updatedChat, "");
            notificationService.get().sendMessage(event.getChatId(), responseMessage);
        }
    }
}
