package com.codifica.chatbot.core.application.ports.in;

import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAceitaResponseEvent;

public interface SolicitacaoAceitaResponseEventListenerPort {
    void processaSolicitacaoAceitaResponse(SolicitacaoAceitaResponseEvent event);
}
