package com.codifica.chatbot.infrastructure.useCaseConfig;

import com.codifica.chatbot.core.application.chat_steps.IdleStepHandler;
import com.codifica.chatbot.core.application.chat_steps.cliente_event.*;
import com.codifica.chatbot.core.application.chat_steps.pet_event.*;
import com.codifica.chatbot.core.application.chat_steps.solicitacao_event.*;
import com.codifica.chatbot.core.application.ports.out.ClienteEventPublisherPort;
import com.codifica.chatbot.core.application.ports.out.PetEventPublisherPort;
import com.codifica.chatbot.core.application.ports.out.SolicitacaoAceitaEventPublisherPort;
import com.codifica.chatbot.core.application.ports.out.SolicitacaoEventPublisherPort;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.infrastructure.services.MainBackendService;
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
    public ConversationStep waitingForPetEventStepHandler(PetEventPublisherPort publisher, MainBackendService mainBackendService) {
        return new WaitingForPetEventStepHandler(publisher, mainBackendService);
    }

    @Bean
    public ConversationStep askForPetStepHandler(MainBackendService mainBackendService) {
        return new AskForPetStepHandler(mainBackendService);
    }

    @Bean
    public ConversationStep listAvailableDaysStepHandler(MainBackendService mainBackendService) {
        return new ListAvailableDaysStepHandler(mainBackendService);
    }

    @Bean
    public ConversationStep listAvailableTimesStepHandler(MainBackendService mainBackendService) {
        return new ListAvailableTimesStepHandler(mainBackendService);
    }

    @Bean
    public ConversationStep listServicesStepHandler(MainBackendService mainBackendService) {
        return new ListServicesStepHandler(mainBackendService);
    }

    @Bean
    public ConversationStep publishSolicitationStepHandler(SolicitacaoEventPublisherPort solicitacaoEventPublisherPort) {
        return new PublishSolicitationStepHandler(solicitacaoEventPublisherPort);
    }

    @Bean
    public ConversationStep solicitacaoAtualizadaStepHandler() {
        return new SolicitacaoAtualizadaStepHandler();
    }

    @Bean
    public ConversationStep clientConfirmationStepHandler(SolicitacaoAceitaEventPublisherPort solicitacaoAceitaEventPublisherPort, UpdateChatUseCase updateChatUseCase) {
        return new ClientConfirmationStepHandler(solicitacaoAceitaEventPublisherPort, updateChatUseCase);
    }

    @Bean
    public ConversationStep responseForSolicitationAcceptStepHandler() {
        return new ResponseForSolicitationAcceptStepHandler();
    }

    @Bean
    public ConversationStep askForBreedSuggestionStepHandler(PetEventPublisherPort petEventPublisherPort) {
        return new AskForBreedSuggestionStepHandler(petEventPublisherPort);
    }

    @Bean
    public ConversationStep idleStepHandler() {
        return new IdleStepHandler();
    }
}
