package com.codifica.chatbot.infrastructure.config;

import com.codifica.chatbot.core.application.usecase.ListChatUseCase;
import com.codifica.chatbot.core.application.usecase.ClienteParaCadastrarResponseUseCase;
import com.codifica.chatbot.core.application.usecase.SalvarChatUseCase;
import com.codifica.chatbot.core.domain.chat.ChatRepository;
import com.codifica.chatbot.infrastructure.persistence.chat_cliente.ChatClienteJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public SalvarChatUseCase salvarChatUseCase(ChatRepository chatRepository) {
        return new SalvarChatUseCase(chatRepository);
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
}
