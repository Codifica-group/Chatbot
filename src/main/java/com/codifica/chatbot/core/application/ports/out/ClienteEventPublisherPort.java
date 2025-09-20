package com.codifica.chatbot.core.application.ports.out;

import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarEvent;

public interface ClienteEventPublisherPort {
    void publishClienteParaCadastrar(ClienteParaCadastrarEvent event);
}
