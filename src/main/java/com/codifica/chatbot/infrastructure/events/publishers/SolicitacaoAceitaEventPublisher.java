package com.codifica.chatbot.infrastructure.events.publishers;

import com.codifica.chatbot.core.application.ports.out.SolicitacaoAceitaEventPublisherPort;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAceitaEvent;
import com.codifica.chatbot.infrastructure.rabbitMQ.RabbitMQConfig;
import com.codifica.chatbot.infrastructure.rabbitMQ.SolicitacaoRabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoAceitaEventPublisher implements SolicitacaoAceitaEventPublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoAceitaEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public SolicitacaoAceitaEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishSolicitacaoAceita(SolicitacaoAceitaEvent event) {
        logger.info("EVENTO PUBLICADO: resposta Solicitacao com solicitacaoId: {}", event.getSolicitacaoId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                SolicitacaoRabbitMQConfig.ROUTING_KEY_SOLICITACAO_ACEITA,
                event
        );
    }
}
