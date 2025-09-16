package com.codifica.chatbot.core.application.usecase;

import com.codifica.chatbot.core.application.ports.in.ChatEventPort;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ChatRepository;

public class SalvarChatUseCase implements ChatEventPort {

    private final ChatRepository chatRepository;

    public SalvarChatUseCase(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }
}
