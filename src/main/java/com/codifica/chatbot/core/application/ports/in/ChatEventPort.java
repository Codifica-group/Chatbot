package com.codifica.chatbot.core.application.ports.in;

import com.codifica.chatbot.core.domain.chat.Chat;

public interface ChatEventPort {
    Chat save(Chat chat);
}
