package com.codifica.chatbot.infrastructure.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolicitacaoRabbitMQConfig {

    public static final String QUEUE_SOLICITACAO_PARA_CADASTRAR = "solicitacao.para-cadastrar.queue";
    public static final String ROUTING_KEY_SOLICITACAO_PARA_CADASTRAR = "solicitacao.para-cadastrar";
    public static final String QUEUE_SOLICITACAO_RESPONSE = "solicitacao.cadastro.response.queue";
    public static final String ROUTING_KEY_SOLICITACAO_RESPONSE = "solicitacao.cadastro.response";
    public static final String QUEUE_SOLICITACAO_ATUALIZADA = "solicitacao.atualizada.queue";
    public static final String ROUTING_KEY_SOLICITACAO_ATUALIZADA = "solicitacao.atualizada";
    public static final String QUEUE_SOLICITACAO_PARA_ATUALIZAR = "solicitacao.para-atualizar.queue";
    public static final String ROUTING_KEY_SOLICITACAO_PARA_ATUALIZAR = "solicitacao.para-atualizar";

    @Bean
    public Queue solicitacaoParaCadastrarQueue() {
        return new Queue(QUEUE_SOLICITACAO_PARA_CADASTRAR, true);
    }

    @Bean
    public Queue solicitacaoResponseQueue() {
        return new Queue(QUEUE_SOLICITACAO_RESPONSE, true);
    }

    @Bean
    public Binding solicitacaoParaCadastrarBinding(Queue solicitacaoParaCadastrarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(solicitacaoParaCadastrarQueue).to(exchange).with(ROUTING_KEY_SOLICITACAO_PARA_CADASTRAR);
    }

    @Bean
    public Binding solicitacaoResponseBinding(Queue solicitacaoResponseQueue, TopicExchange exchange) {
        return BindingBuilder.bind(solicitacaoResponseQueue).to(exchange).with(ROUTING_KEY_SOLICITACAO_RESPONSE);
    }

    @Bean
    public Queue solicitacaoAtualizadaQueue() {
        return new Queue(QUEUE_SOLICITACAO_ATUALIZADA, true);
    }

    @Bean
    public Binding solicitacaoAtualizadaBinding(Queue solicitacaoAtualizadaQueue, TopicExchange exchange) {
        return BindingBuilder.bind(solicitacaoAtualizadaQueue).to(exchange).with(ROUTING_KEY_SOLICITACAO_ATUALIZADA);
    }

    @Bean
    public Queue solicitacaoParaAtualizarQueue() {
        return new Queue(QUEUE_SOLICITACAO_PARA_ATUALIZAR, true);
    }

    @Bean
    public Binding solicitacaoParaAtualizarBinding(Queue solicitacaoParaAtualizarQueue, TopicExchange exchange) {
        return BindingBuilder.bind(solicitacaoParaAtualizarQueue).to(exchange).with(ROUTING_KEY_SOLICITACAO_PARA_ATUALIZAR);
    }
}
