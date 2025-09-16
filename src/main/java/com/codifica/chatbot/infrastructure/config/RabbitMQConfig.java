package com.codifica.chatbot.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "cliente_exchange";
    public static final String QUEUE_NAME = "cliente.para-cadastrar.queue";
    public static final String ROUTING_KEY = "cliente.para-cadastrar";
    public static final String QUEUE_CLIENTE_CADASTRADO = "cliente.cadastrado.queue";
    public static final String QUEUE_FALHA_CADASTRO = "cliente.falha-cadastro.queue";
    public static final String ROUTING_KEY_CLIENTE_CADASTRADO = "cliente.cadastrado";
    public static final String ROUTING_KEY_FALHA_CADASTRO = "cliente.falha-cadastro";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    Queue clienteCadastradoQueue() {
        return new Queue(QUEUE_CLIENTE_CADASTRADO, true);
    }

    @Bean
    Queue falhaCadastroQueue() {
        return new Queue(QUEUE_FALHA_CADASTRO, true);
    }

    @Bean
    Binding clienteCadastradoBinding(Queue clienteCadastradoQueue, TopicExchange exchange) {
        return BindingBuilder.bind(clienteCadastradoQueue).to(exchange).with(ROUTING_KEY_CLIENTE_CADASTRADO);
    }

    @Bean
    Binding falhaCadastroBinding(Queue falhaCadastroQueue, TopicExchange exchange) {
        return BindingBuilder.bind(falhaCadastroQueue).to(exchange).with(ROUTING_KEY_FALHA_CADASTRO);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
