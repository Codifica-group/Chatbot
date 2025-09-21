package com.codifica.chatbot.infrastructure.events.listeners;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoEventListenerPort;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarResponseEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoEventListener {

    private final SolicitacaoEventListenerPort solicitacaoEventListenerPort;

    public SolicitacaoEventListener(SolicitacaoEventListenerPort solicitacaoEventListenerPort) {
        this.solicitacaoEventListenerPort = solicitacaoEventListenerPort;
    }

    @RabbitListener(queues = "solicitacao.cadastro.response.queue")
    public void onSolicitacaoParaCadastrarResponse(SolicitacaoParaCadastrarResponseEvent event) {
        solicitacaoEventListenerPort.processSolicitacaoParaCadastrarResponse(event);
    }
}
