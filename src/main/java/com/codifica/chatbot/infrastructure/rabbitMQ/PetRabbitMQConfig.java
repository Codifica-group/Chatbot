package com.codifica.chatbot.infrastructure.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PetRabbitMQConfig {

    public static final String QUEUE_PET_PARA_CADASTRAR = "pet.para-cadastrar.queue";
    public static final String ROUTING_KEY_PET_PARA_CADASTRAR = "pet.para-cadastrar";
    public static final String QUEUE_PET_RESPONSE = "pet.cadastro.response.queue";
    public static final String ROUTING_KEY_PET_RESPONSE = "pet.cadastro.response";

    @Bean
    public Queue petParaCadastrarQueue() {
        return new Queue(QUEUE_PET_PARA_CADASTRAR, true);
    }

    @Bean
    public Queue petResponseQueue() {
        return new Queue(QUEUE_PET_RESPONSE, true);
    }

    @Bean
    public Binding petParaCadastrarBinding(Queue petParaCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(petParaCadastrarQueue).to(exchange).with(ROUTING_KEY_PET_PARA_CADASTRAR);
    }

    @Bean
    public Binding petResponseBinding(Queue petResponseQueue, TopicExchange exchange) {
        return BindingBuilder.bind(petResponseQueue).to(exchange).with(ROUTING_KEY_PET_RESPONSE);
    }
}
