package com.codifica.chatbot.core.application.ports.in;

import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarResponseEvent;

public interface PetEventListenerPort {
    void processPetParaCadastrarResponse(PetParaCadastrarResponseEvent event);
}
