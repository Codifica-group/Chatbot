package com.codifica.chatbot.infrastructure.adapters;

import com.codifica.chatbot.core.application.ports.in.ProcessarCadastroClienteSucessoUseCasePort;
import com.codifica.chatbot.core.domain.model.events.cliente.CadastroClienteResponseEvent;
import com.codifica.chatbot.core.domain.shared.StatusEvent;
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

    @RabbitListener(queues = "cliente.cadastro.response.queue")
    public void onCadastroClienteResponse(CadastroClienteResponseEvent event) {
        if (event.getStatus() == StatusEvent.SUCESSO) {
            logger.info("SUCESSO: Cliente para o chatId {} cadastrado com clienteId {}.", event.getChatId(), event.getClienteId());
            processarCadastroUseCase.processar(event.getChatId(), event.getClienteId());
        } else {
            logger.error("FALHA: Ocorreu um erro ao cadastrar o cliente para o chatId {}: {}",
                    event.getChatId(), event.getErro());
            // TODO: Lógica para tratativa de erro.
        }
    }
}
