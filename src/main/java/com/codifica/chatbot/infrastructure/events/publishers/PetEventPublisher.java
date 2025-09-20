package com.codifica.chatbot.infrastructure.events.publishers;

import com.codifica.chatbot.core.application.ports.out.PetEventPublisherPort;
import com.codifica.chatbot.core.domain.events.pet.PetParaCadastrarEvent;
import com.codifica.chatbot.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PetEventPublisher implements PetEventPublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(PetEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public PetEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishPetParaCadastrar(PetParaCadastrarEvent event) {
        logger.info("EVENTO PUBLICADO: Pet Para Cadastrar com chatId: {}", event.getChatId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_PET_PARA_CADASTRAR,
                event
        );
    }
}
