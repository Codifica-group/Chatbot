package com.codifica.chatbot.infrastructure.events.publishers;

import com.codifica.chatbot.core.application.ports.out.ClienteEventPublisherPort;
import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarEvent;
import com.codifica.chatbot.infrastructure.rabbitMQ.ClienteRabbitMQConfig;
import com.codifica.chatbot.infrastructure.rabbitMQ.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClienteEventPublisher implements ClienteEventPublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(ClienteEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public ClienteEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishClienteParaCadastrar(ClienteParaCadastrarEvent event) {
        logger.info("EVENTO PUBLICADO: Cliente Para Cadastrar com chatId: {}", event.getChatId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                ClienteRabbitMQConfig.ROUTING_KEY_PARA_CADASTRAR,
                event
        );
    }
}
