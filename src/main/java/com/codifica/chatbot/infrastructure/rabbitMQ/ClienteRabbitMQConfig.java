package com.codifica.chatbot.infrastructure.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteRabbitMQConfig {

    public static final String QUEUE_PARA_CADASTRAR = "cliente.para-cadastrar.queue";
    public static final String ROUTING_KEY_PARA_CADASTRAR = "cliente.para-cadastrar";
    public static final String QUEUE_CLIENTE_RESPONSE = "cliente.cadastro.response.queue";
    public static final String ROUTING_KEY_CLIENTE_RESPONSE = "cliente.cadastro.response";

    @Bean
    public Queue paraCadastrarQueue() {
        return new Queue(QUEUE_PARA_CADASTRAR, true);
    }

    @Bean
    public Queue clienteResponseQueue() {
        return new Queue(QUEUE_CLIENTE_RESPONSE, true);
    }

    @Bean
    public Binding paraCadastrarBinding(Queue paraCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paraCadastrarQueue).to(exchange).with(ROUTING_KEY_PARA_CADASTRAR);
    }

    @Bean
    public Binding clienteResponseBinding(Queue clienteResponseQueue, TopicExchange exchange) {
        return BindingBuilder.bind(clienteResponseQueue).to(exchange).with(ROUTING_KEY_CLIENTE_RESPONSE);
    }
}
