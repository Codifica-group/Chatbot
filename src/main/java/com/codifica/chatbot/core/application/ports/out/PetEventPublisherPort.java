package com.codifica.chatbot.core.application.ports.out;

import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarEvent;

public interface PetEventPublisherPort {
    void publishPetParaCadastrar(PetParaCadastrarEvent event);
}
