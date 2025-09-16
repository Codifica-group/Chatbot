package com.codifica.chatbot.infrastructure.adapters;

import com.codifica.chatbot.core.application.ports.in.ProcessarCadastroClienteSucessoUseCasePort;
import com.codifica.chatbot.core.domain.model.events.response.ClienteCadastradoEvent;
import com.codifica.chatbot.core.domain.model.events.response.FalhaCadastroClienteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClienteEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ClienteEventListener.class);
    private final ProcessarCadastroClienteSucessoUseCasePort processarCadastroUseCase;

    public ClienteEventListener(ProcessarCadastroClienteSucessoUseCasePort processarCadastroUseCase) {
        this.processarCadastroUseCase = processarCadastroUseCase;
    }

    @RabbitListener(queues = "cliente.cadastrado.queue")
    public void onClienteCadastrado(ClienteCadastradoEvent event) {
        processarCadastroUseCase.processar(event.getChatId(), event.getClienteId());
    }

    @RabbitListener(queues = "cliente.falha-cadastro.queue")
    public void onFalhaCadastro(FalhaCadastroClienteEvent event) {
        logger.error("FALHA: Ocorreu um erro ao cadastrar o cliente para o chatId {}. Motivo: {}",
                event.getChatId(), event.getErro());

        // Próximo passo: Atualizar o estado do chat para um estado de erro
        // e talvez enviar uma mensagem ao usuário pedindo para tentar novamente.
    }
}
