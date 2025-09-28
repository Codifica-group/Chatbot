package com.codifica.chatbot.infrastructure.events.publishers;

import com.codifica.chatbot.core.application.ports.out.SolicitacaoEventPublisherPort;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarEvent;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaAtualizarEvent;
import com.codifica.chatbot.infrastructure.rabbitMQ.RabbitMQConfig;
import com.codifica.chatbot.infrastructure.rabbitMQ.SolicitacaoRabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoEventPublisher implements SolicitacaoEventPublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(SolicitacaoEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public SolicitacaoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishSolicitacaoParaCadastrar(SolicitacaoParaCadastrarEvent event) {
        logger.info("EVENTO PUBLICADO: Solicitacao Para Cadastrar com chatId: {}", event.getChatId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                SolicitacaoRabbitMQConfig.ROUTING_KEY_SOLICITACAO_PARA_CADASTRAR,
                event
        );
    }

    @Override
    public void publishSolicitacaoParaAtualizar(SolicitacaoParaAtualizarEvent event) {
        logger.info("EVENTO PUBLICADO: Solicitacao Para Atualizar com chatId: {}", event.getChatId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                "solicitacao.para-atualizar",
                event
        );
    }
}
