package com.codifica.chatbot.core.application.ports.in;

import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarResponseEvent;

public interface ClienteEventListenerPort {
    void processClienteParaCadastrarResponse(ClienteParaCadastrarResponseEvent event);
}
