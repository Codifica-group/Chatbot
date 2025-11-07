package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.ClienteEventListenerPort;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.cliente.Cliente;
import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
import com.codifica.chatbot.infrastructure.chat.ChatFlowService;
import com.codifica.chatbot.interfaces.adapters.telegram.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

public class ClienteParaCadastrarResponseUseCase implements ClienteEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(ClienteParaCadastrarResponseUseCase.class);
    private final UpdateChatUseCase updateChatUseCase;
    private final FindChatByIdUseCase findChatByIdUseCase;
    private final ChatFlowService chatFlowService;
    private final Optional<NotificationService> notificationService;

    public ClienteParaCadastrarResponseUseCase(UpdateChatUseCase updateChatUseCase,
                                               FindChatByIdUseCase findChatByIdUseCase,
                                               ChatFlowService chatFlowService,
                                               Optional<NotificationService> notificationService) {
        this.updateChatUseCase = updateChatUseCase;
        this.findChatByIdUseCase = findChatByIdUseCase;
        this.chatFlowService = chatFlowService;
        this.notificationService = notificationService;
    }

    @Override
    public void processClienteParaCadastrarResponse(ClienteParaCadastrarResponseEvent event) {
        Chat chat;

        if (event.getStatus() == StatusEvent.SUCESSO) {
            logger.info("SUCESSO: Cliente do chatId {} cadastrado com Id: {}", event.getChatId(), event.getClienteId());
            String passoAtual = "AGUARDANDO_CADASTRO_PET";
            String dadosContexto = "{}";
            Cliente cliente = new Cliente(event.getClienteId(), event.getClienteNome());
            chat = new Chat(event.getChatId(),  passoAtual, dadosContexto, LocalDateTime.now(), cliente);
            updateChatUseCase.updateChatStatus(chat);
            logger.info("ATUALIZAÇÃO: Chat {}, Cliente {}, Passo atual {}.", chat.getId(), chat.getCliente().getId(), chat.getPassoAtual());
        } else {
            logger.error("FALHA: Erro ao cadastrar cliente do chatId {}: {}", event.getChatId(), event.getErro());
//            String passoAtual = "ERRO_CADASTRO_CLIENTE";
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
