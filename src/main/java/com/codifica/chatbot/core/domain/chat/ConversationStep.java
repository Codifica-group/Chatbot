package com.codifica.chatbot.core.domain.chat;

public interface ConversationStep {
    String getStepName();
    StepResponse process(Chat chat, String userMessage);
}
