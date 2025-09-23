package com.codifica.chatbot.core.application.chat_steps.cliente_event;

import com.codifica.chatbot.core.application.ports.out.ClienteEventPublisherPort;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class WaitingForClienteEventStepHandler implements ConversationStep {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ClienteEventPublisherPort clienteEventPublisherPort;

    public WaitingForClienteEventStepHandler(ClienteEventPublisherPort clienteEventPublisherPort) {
        this.clienteEventPublisherPort = clienteEventPublisherPort;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_COMPLEMENTO_CLIENTE";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, String> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            dadosContexto.put("complemento", "não".equalsIgnoreCase(userMessage) ? "" : userMessage);
            chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));

            publishClienteEvent(chat.getId(), dadosContexto);

            String responseMessage = "Perfeito! Já enviei seus dados para o nosso sistema. Um momento enquanto finalizamos seu cadastro.";
            return new StepResponse(responseMessage, "AGUARDANDO_RESPOSTA_CADASTRO_CLIENTE");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar o complemento do seu endereço. Tente novamente.", getStepName());
        }
    }

    private void publishClienteEvent(Integer chatId, Map<String, String> dados) {
        ClienteParaCadastrarEvent event = new ClienteParaCadastrarEvent(
                chatId,
                dados.get("nome"),
                dados.get("telefone"),
                dados.get("cep"),
                dados.get("numeroEndereco"),
                dados.get("complemento")
        );
        clienteEventPublisherPort.publishClienteParaCadastrar(event);
    }
}
