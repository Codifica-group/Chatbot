package com.codifica.chatbot.infrastructure.events.listeners;

import com.codifica.chatbot.core.application.ports.in.ClienteEventListenerPort;
import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarResponseEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClienteEventListener {

    private final ClienteEventListenerPort clienteEventListenerPort;

    public ClienteEventListener(ClienteEventListenerPort clienteEventListenerPort) {
        this.clienteEventListenerPort = clienteEventListenerPort;
    }

    @RabbitListener(queues = "cliente.cadastro.response.queue")
    public void onClienteParaCadastrarResponse(ClienteParaCadastrarResponseEvent event) {
        clienteEventListenerPort.processClienteParaCadastrarResponse(event);
    }
}
