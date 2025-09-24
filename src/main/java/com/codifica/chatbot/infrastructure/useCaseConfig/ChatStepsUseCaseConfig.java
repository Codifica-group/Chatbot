package com.codifica.chatbot.infrastructure.useCaseConfig;

import com.codifica.chatbot.core.application.chat_steps.cliente_event.*;
import com.codifica.chatbot.core.application.chat_steps.pet_event.*;
import com.codifica.chatbot.core.application.ports.out.ClienteEventPublisherPort;
import com.codifica.chatbot.core.application.ports.out.PetEventPublisherPort;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatStepsUseCaseConfig {

    @Bean
    public ConversationStep initialStepHandler() {
        return new InitialStepHandler();
    }

    @Bean
    public ConversationStep askForPhoneNumberStepHandler() {
        return new AskForPhoneNumberStepHandler();
    }

    @Bean
    public ConversationStep askForCepStepHandler() {
        return new AskForCepStepHandler();
    }

    @Bean
    public ConversationStep askForAdressNumberStepHandler() {
        return new AskForAdressNumberStepHandler();
    }

    @Bean
    public ConversationStep askForAdressComplementStepHandler() {
        return new AskForAdressComplementStepHandler();
    }

    @Bean
    public ConversationStep waitingForClienteEventStetpHandler(ClienteEventPublisherPort clienteEventPublisherPort) {
        return new WaitingForClienteEventStepHandler(clienteEventPublisherPort);
    }

    @Bean
    public ConversationStep askForPetNameStepHandler() {
        return new AskForPetNameStepHandler();
    }

    @Bean
    public ConversationStep askForPetBreedStepHandler() {
        return new AskForPetBreedStepHandler();
    }

    @Bean
    public ConversationStep waitingForPetEventStepHandler(PetEventPublisherPort publisher) {
        return new WaitingForPetEventStepHandler(publisher);
    }
}
