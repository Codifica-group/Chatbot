package com.codifica.chatbot.infrastructure.chat;

import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatFlowService {

    private final UpdateChatUseCase updateChatUseCase;
    private final StepHandlerFactory stepHandlerFactory;

    public ChatFlowService(UpdateChatUseCase updateChatUseCase, StepHandlerFactory stepHandlerFactory) {
        this.updateChatUseCase = updateChatUseCase;
        this.stepHandlerFactory = stepHandlerFactory;
    }

    public String processMessage(Chat chat, String userMessage) {
        var handler = stepHandlerFactory.getHandler(chat.getPassoAtual())
                .orElseThrow(() -> new IllegalStateException("Nenhum handler encontrado para o passo: " + chat.getPassoAtual()));

        StepResponse stepResponse = handler.process(chat, userMessage);

        chat.setPassoAtual(stepResponse.nextStep());
        chat.setDataAtualizacao(LocalDateTime.now());
        updateChatUseCase.execute(chat.getId(), chat);

        return stepResponse.responseMessage();
    }
}
