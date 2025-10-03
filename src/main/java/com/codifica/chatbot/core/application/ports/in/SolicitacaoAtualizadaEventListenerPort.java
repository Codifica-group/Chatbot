package com.codifica.chatbot.core.application.ports.in;

import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAtualizadaEvent;

public interface SolicitacaoAtualizadaEventListenerPort {
    void processSolicitacaoAtualizada(SolicitacaoAtualizadaEvent event);
}
