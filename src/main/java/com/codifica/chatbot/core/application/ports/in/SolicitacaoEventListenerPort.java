package com.codifica.chatbot.core.application.ports.in;

import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAtualizadaEvent;

public interface SolicitacaoEventListenerPort {
    void processSolicitacaoParaCadastrarResponse(SolicitacaoParaCadastrarResponseEvent event);
    void processSolicitacaoAtualizada(SolicitacaoAtualizadaEvent event);
}
