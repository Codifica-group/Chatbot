package com.codifica.chatbot.infrastructure.events.listeners;

import com.codifica.chatbot.core.application.ports.in.PetEventListenerPort;
import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarResponseEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PetEventListener {

    private final PetEventListenerPort petEventListenerPort;

    public PetEventListener(PetEventListenerPort petEventListenerPort) {
        this.petEventListenerPort = petEventListenerPort;
    }

    @RabbitListener(queues = "pet.cadastro.response.queue")
    public void onPetParaCadastrarResponse(PetParaCadastrarResponseEvent event) {
        petEventListenerPort.processPetParaCadastrarResponse(event);
    }
}
