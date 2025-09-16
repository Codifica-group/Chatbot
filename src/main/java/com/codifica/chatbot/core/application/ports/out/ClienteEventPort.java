package com.codifica.chatbot.core.application.ports.out;

import com.codifica.chatbot.core.domain.model.events.ClienteParaCadastrarEvent;

public interface ClienteEventPort {
    void publishClientToRegister(ClienteParaCadastrarEvent event);
}
