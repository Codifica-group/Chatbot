package com.codifica.chatbot.infrastructure.chat;

import com.codifica.chatbot.core.domain.chat.ConversationStep;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StepHandlerFactory {

    private final Map<String, ConversationStep> handlers;

    public StepHandlerFactory(List<ConversationStep> stepHandlers) {
        this.handlers = stepHandlers.stream()
                .collect(Collectors.toUnmodifiableMap(ConversationStep::getStepName, Function.identity()));
    }

    public Optional<ConversationStep> getHandler(String stepName) {
        return Optional.ofNullable(handlers.get(stepName));
    }
}
