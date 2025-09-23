package com.codifica.chatbot.infrastructure.useCaseConfig;

import com.codifica.chatbot.core.application.usecase.chat.*;
import com.codifica.chatbot.core.domain.chat.ChatRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatUseCaseConfig {

    @Bean
    public CreateChatUseCase createChatUseCase(ChatRepository chatRepository) {
        return new CreateChatUseCase(chatRepository);
    }

    @Bean
    public ListChatUseCase listChatUseCase(ChatRepository chatRepository) {
        return new ListChatUseCase(chatRepository);
    }

    @Bean
    public FindChatByIdUseCase findChatByIdUseCase(ChatRepository chatRepository) {
        return new FindChatByIdUseCase(chatRepository);
    }

    @Bean
    public UpdateChatUseCase updateChatUseCase(ChatRepository chatRepository) {
        return new UpdateChatUseCase(chatRepository);
    }

    @Bean
    public DeleteChatUseCase deleteChatUseCase(ChatRepository chatRepository) {
        return new DeleteChatUseCase(chatRepository);
    }
}
