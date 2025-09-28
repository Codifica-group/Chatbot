package com.codifica.chatbot.core.application.ports.out;

import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarEvent;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaAtualizarEvent;

public interface SolicitacaoEventPublisherPort {
    void publishSolicitacaoParaCadastrar(SolicitacaoParaCadastrarEvent event);
    void publishSolicitacaoParaAtualizar(SolicitacaoParaAtualizarEvent event);
}
