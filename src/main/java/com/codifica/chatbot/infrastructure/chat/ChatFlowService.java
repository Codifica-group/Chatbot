package com.codifica.chatbot.infrastructure.chat;

import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class ChatFlowService {

    private final UpdateChatUseCase updateChatUseCase;
    private final StepHandlerFactory stepHandlerFactory;
    private static final Logger logger = LoggerFactory.getLogger(ChatFlowService.class);

    public ChatFlowService(UpdateChatUseCase updateChatUseCase, StepHandlerFactory stepHandlerFactory) {
        this.updateChatUseCase = updateChatUseCase;
        this.stepHandlerFactory = stepHandlerFactory;
    }

    public String processMessage(Chat chat, String userMessage) {
        try {
            String currentStepName = chat.getPassoAtual();
            var handler = stepHandlerFactory.getHandler(chat.getPassoAtual())
                    .orElseThrow(() -> new IllegalStateException("Nenhum handler encontrado para o passo: " + chat.getPassoAtual()));

            StepResponse stepResponse = handler.process(chat, userMessage);

            chat.setPassoAtual(stepResponse.nextStep());
            chat.setDataAtualizacao(LocalDateTime.now());
            String finalResponseMessage = stepResponse.responseMessage();

            if ("IDLE".equals(currentStepName)) {
                var nextHandler = stepHandlerFactory.getHandler(chat.getPassoAtual())
                        .orElseThrow(() -> new IllegalStateException("Nenhum handler encontrado para o passo seguinte ao IDLE: " + chat.getPassoAtual()));

                StepResponse nextStepResponse = nextHandler.process(chat, "");
                chat.setPassoAtual(nextStepResponse.nextStep());
                finalResponseMessage += "\n" + nextStepResponse.responseMessage();
            }

            updateChatUseCase.execute(chat.getId(), chat);

            return finalResponseMessage;

        } catch (Exception e) {
            logger.error("ERRO INESPERADO: Falha ao processar mensagem para o Chat ID {} Passo atual {} DadosContexto: {}. Erro: {}",
                    chat.getId(), chat.getPassoAtual(), chat.getDadosContexto(), e.getMessage(), e);

            var errorHandler = stepHandlerFactory.getHandler("ERRO")
                    .orElseThrow(() -> new IllegalStateException("Nenhum handler encontrado para o passo: ERRO"));

            StepResponse errorResponse = errorHandler.process(chat, "");

            chat.setPassoAtual(errorResponse.nextStep());
            chat.setDadosContexto("{}");
            chat.setDataAtualizacao(LocalDateTime.now());
            updateChatUseCase.execute(chat.getId(), chat);

            return errorResponse.responseMessage();
        }
    }
}
