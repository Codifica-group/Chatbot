package com.codifica.chatbot.infrastructure.config;

import com.codifica.chatbot.core.application.usecase.ListChatUseCase;
import com.codifica.chatbot.core.application.usecase.ProcessarCadastroClienteSucessoUseCase;
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
    public ProcessarCadastroClienteSucessoUseCase processarCadastroClienteSucessoUseCase(
            ChatRepository chatRepository,
            ChatClienteJpaRepository chatClienteJpaRepository) {
        return new ProcessarCadastroClienteSucessoUseCase(chatRepository, chatClienteJpaRepository);
    }
}
