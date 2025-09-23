package com.codifica.chatbot.infrastructure.useCaseConfig;

import com.codifica.chatbot.core.application.usecase.*;
import com.codifica.chatbot.core.domain.chat.ChatRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventUseCaseConfig {

    @Bean
    public ClienteParaCadastrarResponseUseCase processClienteParaCadastrarResponseUseCase(
            ChatRepository chatRepository) {
        return new ClienteParaCadastrarResponseUseCase(chatRepository);
    }

    @Bean
    public PetParaCadastrarResponseUseCase processPetParaCadastrarResponseUseCase(
            ChatRepository chatRepository) {
        return new PetParaCadastrarResponseUseCase(chatRepository);
    }

    @Bean
    public SolicitacaoParaCadastrarResponseUseCase processSolicitacaoParaCadastrarResponseUseCase(ChatRepository chatRepository) {
        return new SolicitacaoParaCadastrarResponseUseCase(chatRepository);
    }
}
