package com.codifica.chatbot.infrastructure.adapters;

import com.codifica.chatbot.core.application.ports.out.ClienteEventPort;
import com.codifica.chatbot.core.domain.model.events.cliente.ClienteParaCadastrarEvent;
import com.codifica.chatbot.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClienteEventPublisher implements ClienteEventPort {

    private static final Logger logger = LoggerFactory.getLogger(ClienteEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public ClienteEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishClientToRegister(ClienteParaCadastrarEvent event) {
        logger.info("Publicando evento para cadastro de cliente com chatId: {}", event.getChatId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_PARA_CADASTRAR,
                event
        );
    }
}
