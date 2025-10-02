package com.codifica.chatbot.core.application.ports.out;

import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarEvent;

public interface SolicitacaoEventPublisherPort {
    void publishSolicitacaoParaCadastrar(SolicitacaoParaCadastrarEvent event);
}
