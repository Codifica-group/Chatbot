package com.codifica.chatbot.infrastructure.useCaseConfig;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoAtualizadaEventListenerPort;
import com.codifica.chatbot.core.application.usecase.ClienteParaCadastrarResponseUseCase;
import com.codifica.chatbot.core.application.usecase.PetParaCadastrarResponseUseCase;
import com.codifica.chatbot.core.application.usecase.SolicitacaoAtualizadaUseCase;
import com.codifica.chatbot.core.application.usecase.SolicitacaoParaCadastrarResponseUseCase;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventUseCaseConfig {

    @Bean
    public ClienteParaCadastrarResponseUseCase processClienteParaCadastrarResponseUseCase(
            UpdateChatUseCase updateChatUseCase) {
        return new ClienteParaCadastrarResponseUseCase(updateChatUseCase);
    }

    @Bean
    public PetParaCadastrarResponseUseCase processPetParaCadastrarResponseUseCase(
            UpdateChatUseCase updateChatUseCase) {
        return new PetParaCadastrarResponseUseCase(updateChatUseCase);
    }

    @Bean
    public SolicitacaoParaCadastrarResponseUseCase processSolicitacaoParaCadastrarResponseUseCase(
            UpdateChatUseCase updateChatUseCase) {
        return new SolicitacaoParaCadastrarResponseUseCase(updateChatUseCase);
    }

    @Bean
    public SolicitacaoAtualizadaEventListenerPort solicitacaoAtualizadaUseCase(UpdateChatUseCase updateChatUseCase, ObjectMapper objectMapper) {
        return new SolicitacaoAtualizadaUseCase(updateChatUseCase, objectMapper);
    }
}
