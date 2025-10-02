package com.codifica.chatbot.core.application.ports.in;

import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarResponseEvent;

public interface SolicitacaoEventListenerPort {
    void processSolicitacaoParaCadastrarResponse(SolicitacaoParaCadastrarResponseEvent event);
}
