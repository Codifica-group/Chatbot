package com.codifica.chatbot.core.application.ports.out;

import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAceitaEvent;

public interface SolicitacaoAceitaEventPublisherPort {
    void publishSolicitacaoAceita(SolicitacaoAceitaEvent event);
}
