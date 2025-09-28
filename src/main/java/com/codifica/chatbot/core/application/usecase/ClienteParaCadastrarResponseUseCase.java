package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.ClienteEventListenerPort;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.cliente.Cliente;
import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class ClienteParaCadastrarResponseUseCase implements ClienteEventListenerPort {

    private static final Logger logger = LoggerFactory.getLogger(ClienteParaCadastrarResponseUseCase.class);
    private final UpdateChatUseCase updateChatUseCase;

    public ClienteParaCadastrarResponseUseCase(UpdateChatUseCase updateChatUseCase) {
        this.updateChatUseCase = updateChatUseCase;
    }

    @Override
    public void processClienteParaCadastrarResponse(ClienteParaCadastrarResponseEvent event) {
        if (event.getStatus() == StatusEvent.SUCESSO) {
            logger.info("SUCESSO: Cliente do chatId {} cadastrado com Id: {}", event.getChatId(), event.getClienteId());
            String passoAtual = "AGUARDANDO_CADASTRO_PET";
            String dadosContexto = "{}";
            Cliente cliente = new Cliente(event.getClienteId(), event.getClienteNome());
            Chat chat = new Chat(event.getChatId(),  passoAtual, dadosContexto, LocalDateTime.now(), cliente);
            updateChatUseCase.updateChatStatus(chat);
            logger.info("ATUALIZAÇÃO: Chat {}, Cliente {}, Passo atual {}.", chat.getId(), chat.getCliente().getId(), chat.getPassoAtual());
        } else {
            logger.error("FALHA: Erro ao cadastrar cliente do chatId {}: {}", event.getChatId(), event.getErro());
            String passoAtual = "ERRO_CADASTRO_CLIENTE";
            String dadosContexto = "{\"erro\": \"" + event.getErro() + "\"}";
            Chat chat = new Chat(event.getChatId(), passoAtual, dadosContexto, LocalDateTime.now(), null);
            updateChatUseCase.updateChatStatus(chat);
        }
    }
}
