package com.codifica.chatbot.infrastructure.useCaseConfig;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoAceitaResponseEventListenerPort;
import com.codifica.chatbot.core.application.ports.in.SolicitacaoAtualizadaEventListenerPort;
import com.codifica.chatbot.core.application.usecase.*;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.infrastructure.chat.ChatFlowService;
import com.codifica.chatbot.interfaces.adapters.telegram.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Optional;

@Configuration
public class EventUseCaseConfig {

    @Bean
    public ClienteParaCadastrarResponseUseCase processClienteParaCadastrarResponseUseCase(
            UpdateChatUseCase updateChatUseCase,
            FindChatByIdUseCase findChatByIdUseCase,
            ChatFlowService chatFlowService,
            Optional<NotificationService> notificationService) {
        return new ClienteParaCadastrarResponseUseCase(updateChatUseCase, findChatByIdUseCase, chatFlowService, notificationService);
    }

    @Bean
    public PetParaCadastrarResponseUseCase processPetParaCadastrarResponseUseCase(
            UpdateChatUseCase updateChatUseCase,
            FindChatByIdUseCase findChatByIdUseCase,
            ChatFlowService chatFlowService,
            Optional<NotificationService> notificationService) {
        return new PetParaCadastrarResponseUseCase(updateChatUseCase, findChatByIdUseCase, chatFlowService, notificationService);
    }

    @Bean
    public SolicitacaoParaCadastrarResponseUseCase processSolicitacaoParaCadastrarResponseUseCase(
            UpdateChatUseCase updateChatUseCase) {
        return new SolicitacaoParaCadastrarResponseUseCase(updateChatUseCase);
    }

    @Bean
    public SolicitacaoAtualizadaEventListenerPort solicitacaoAtualizadaUseCase(
            UpdateChatUseCase updateChatUseCase,
            ObjectMapper objectMapper,
            FindChatByIdUseCase findChatByIdUseCase,
            ChatFlowService chatFlowService,
            Optional<NotificationService> notificationService) {
        return new SolicitacaoAtualizadaUseCase(updateChatUseCase, objectMapper, findChatByIdUseCase, chatFlowService, notificationService);
    }

    @Bean
    public SolicitacaoAceitaResponseEventListenerPort solicitacaoAceitaResponseUseCase(
            FindChatByIdUseCase findChatByIdUseCase,
            UpdateChatUseCase updateChatUseCase,
            ChatFlowService chatFlowService,
            Optional<NotificationService> notificationService) {
        return new SolicitacaoAceitaResponseUseCase(findChatByIdUseCase, updateChatUseCase, chatFlowService, notificationService);
    }
}
