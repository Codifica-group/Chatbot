package com.codifica.chatbot.infrastructure.useCaseConfig;

import com.codifica.chatbot.core.application.usecase.ClienteParaCadastrarResponseUseCase;
import com.codifica.chatbot.core.application.usecase.PetParaCadastrarResponseUseCase;
import com.codifica.chatbot.core.application.usecase.SolicitacaoResponseUseCase;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
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
    public SolicitacaoResponseUseCase processSolicitacaoParaCadastrarResponseUseCase(
            UpdateChatUseCase updateChatUseCase) {
        return new SolicitacaoResponseUseCase(updateChatUseCase);
    }
}
