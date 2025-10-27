package com.codifica.chatbot.interfaces.controller;

import com.codifica.chatbot.core.application.ports.out.ClienteEventPublisherPort;
import com.codifica.chatbot.core.application.ports.out.PetEventPublisherPort;
import com.codifica.chatbot.core.application.ports.out.SolicitacaoEventPublisherPort;
import com.codifica.chatbot.core.application.usecase.chat.*;
import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarEvent;
import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarEvent;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot/api/events")
public class EventController {

    private final ClienteEventPublisherPort clienteEventPublisherPort;
    private final UpdateChatUseCase updateChatUseCase;
    private final PetEventPublisherPort petEventPublisherPort;
    private final SolicitacaoEventPublisherPort solicitacaoEventPublisherPort;

    public EventController(ClienteEventPublisherPort clienteEventPublisherPort,
                           PetEventPublisherPort petEventPublisherPort,
                           UpdateChatUseCase updateChatUseCase,
                           SolicitacaoEventPublisherPort solicitacaoEventPublisherPort) {
        this.clienteEventPublisherPort = clienteEventPublisherPort;
        this.petEventPublisherPort = petEventPublisherPort;
        this.updateChatUseCase = updateChatUseCase;
        this.solicitacaoEventPublisherPort = solicitacaoEventPublisherPort;
    }

    @PostMapping("/cliente-para-cadastrar")
    public ResponseEntity<String> dispararEventoCliente(@RequestBody ClienteParaCadastrarEvent event) {
        clienteEventPublisherPort.publishClienteParaCadastrar(event);
        return ResponseEntity.ok("Evento publicado.");
    }

    @PostMapping("/pet-para-cadastrar")
    public ResponseEntity<String> dispararEventoPet(@RequestBody PetParaCadastrarEvent event) {
        petEventPublisherPort.publishPetParaCadastrar(event);
        return ResponseEntity.ok("Evento publicado.");
    }

    @PostMapping("/solicitacao-para-cadastrar")
    public ResponseEntity<String> dispararEventoSolicitacao(@RequestBody SolicitacaoParaCadastrarEvent event) {
        solicitacaoEventPublisherPort.publishSolicitacaoParaCadastrar(event);
        return ResponseEntity.ok("Evento publicado.");
    }
}
