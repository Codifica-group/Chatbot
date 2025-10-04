package com.codifica.chatbot.infrastructure.events.listeners;

import com.codifica.chatbot.core.application.ports.in.SolicitacaoAceitaResponseEventListenerPort;
import com.codifica.chatbot.core.application.ports.in.SolicitacaoAtualizadaEventListenerPort;
import com.codifica.chatbot.core.application.ports.in.SolicitacaoEventListenerPort;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAceitaResponseEvent;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarResponseEvent;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAtualizadaEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoEventListener {

    private final SolicitacaoEventListenerPort solicitacaoEventListenerPort;
    private final SolicitacaoAtualizadaEventListenerPort solicitacaoAtualizadaEventListenerPort;
    private final SolicitacaoAceitaResponseEventListenerPort solicitacaoAceitaResponseEventListenerPort;

    public SolicitacaoEventListener(SolicitacaoEventListenerPort solicitacaoEventListenerPort,
                                    SolicitacaoAtualizadaEventListenerPort solicitacaoAtualizadaEventListenerPort,
                                    SolicitacaoAceitaResponseEventListenerPort solicitacaoAceitaResponseEventListenerPort) {
        this.solicitacaoEventListenerPort = solicitacaoEventListenerPort;
        this.solicitacaoAtualizadaEventListenerPort = solicitacaoAtualizadaEventListenerPort;
        this.solicitacaoAceitaResponseEventListenerPort = solicitacaoAceitaResponseEventListenerPort;
    }

    @RabbitListener(queues = "solicitacao.cadastro.response.queue")
    public void onSolicitacaoParaCadastrarResponse(SolicitacaoParaCadastrarResponseEvent event) {
        solicitacaoEventListenerPort.processSolicitacaoParaCadastrarResponse(event);
    }

    @RabbitListener(queues = "solicitacao.atualizada.queue")
    public void onSolicitacaoAtualizada(SolicitacaoAtualizadaEvent event) {
        solicitacaoAtualizadaEventListenerPort.processSolicitacaoAtualizada(event);
    }

    @RabbitListener(queues = "solicitacao.aceita.response.queue")
    public void onSolicitacaoAceitaResponse(SolicitacaoAceitaResponseEvent event) {
        solicitacaoAceitaResponseEventListenerPort.processaSolicitacaoAceitaResponse(event);
    }
}
