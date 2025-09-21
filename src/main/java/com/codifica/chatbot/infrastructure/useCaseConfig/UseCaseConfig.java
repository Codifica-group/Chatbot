package com.codifica.chatbot.infrastructure.useCaseConfig;

import com.codifica.chatbot.core.application.usecase.*;
import com.codifica.chatbot.core.application.usecase.chat.*;
import com.codifica.chatbot.core.domain.chat.ChatRepository;
import com.codifica.chatbot.infrastructure.persistence.chat_cliente.ChatClienteJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateChatUseCase createChatUseCase(ChatRepository chatRepository) {
        return new CreateChatUseCase(chatRepository);
    }

    @Bean
    public ListChatUseCase listChatUseCase(ChatRepository chatRepository) {
        return new ListChatUseCase(chatRepository);
    }

    @Bean
    public ClienteParaCadastrarResponseUseCase processClienteParaCadastrarResponseUseCase(
            ChatRepository chatRepository,
            ChatClienteJpaRepository chatClienteJpaRepository) {
        return new ClienteParaCadastrarResponseUseCase(chatRepository, chatClienteJpaRepository);
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
