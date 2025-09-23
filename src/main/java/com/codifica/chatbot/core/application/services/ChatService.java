package com.codifica.chatbot.core.application.services;

import com.codifica.chatbot.core.application.ports.out.ClienteEventPublisherPort;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {

    private final UpdateChatUseCase updateChatUseCase;
    private final ClienteEventPublisherPort clienteEventPublisherPort;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatService(UpdateChatUseCase updateChatUseCase, ClienteEventPublisherPort clienteEventPublisherPort) {
        this.updateChatUseCase = updateChatUseCase;
        this.clienteEventPublisherPort = clienteEventPublisherPort;
    }

    public String processMessage(Chat chat, String userMessage) {
        String responseMessage = "Desculpe, não entendi. Poderia repetir?";
        String passoAtual = chat.getPassoAtual();

        try {
            Map<String, String> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), HashMap.class);

            switch (passoAtual) {
                case "INICIO":
                    chat.setPassoAtual("AGUARDANDO_NOME_CLIENTE");
                    responseMessage = "Olá! Bem-vindo ao nosso pet shop. Para começarmos, qual é o seu nome completo?";
                    break;

                case "AGUARDANDO_NOME_CLIENTE":
                    dadosContexto.put("nome", userMessage);
                    chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));
                    chat.setPassoAtual("AGUARDANDO_TELEFONE_CLIENTE");
                    responseMessage = "Obrigado, " + userMessage.split(" ")[0] + "! Agora, por favor, digite seu telefone com DDD (apenas números).";
                    break;

                case "AGUARDANDO_TELEFONE_CLIENTE":
                    dadosContexto.put("telefone", userMessage);
                    chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));
                    chat.setPassoAtual("AGUARDANDO_CEP_CLIENTE");
                    responseMessage = "Ok! E qual o seu CEP (apenas números)?";
                    break;

                case "AGUARDANDO_CEP_CLIENTE":
                    dadosContexto.put("cep", userMessage);
                    chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));
                    chat.setPassoAtual("AGUARDANDO_NUMERO_ENDERECO_CLIENTE");
                    responseMessage = "Entendi. Qual o número do seu endereço?";
                    break;

                case "AGUARDANDO_NUMERO_ENDERECO_CLIENTE":
                    dadosContexto.put("numeroEndereco", userMessage);
                    chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));
                    chat.setPassoAtual("AGUARDANDO_COMPLEMENTO_CLIENTE");
                    responseMessage = "Estamos quase lá! Você tem algum complemento (ex: Apto 101, Bloco C)? Se não tiver, pode digitar 'não'.";
                    break;

                case "AGUARDANDO_COMPLEMENTO_CLIENTE":
                    dadosContexto.put("complemento", "não".equalsIgnoreCase(userMessage) ? null : userMessage);
                    chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));

                    publicarEventoCliente(chat.getId(), dadosContexto);

                    chat.setPassoAtual("AGUARDANDO_RESPOSTA_CADASTRO_CLIENTE");
                    responseMessage = "Perfeito! Já enviei seus dados para o nosso sistema. Um momento enquanto finalizamos seu cadastro.";
                    break;
            }
        } catch (JsonProcessingException e) {
            responseMessage = "Ocorreu um erro interno ao processar seus dados. Tente novamente mais tarde.";
            // TODO: Tratar o erro
        }

        chat.setDataAtualizacao(LocalDateTime.now());
        updateChatUseCase.execute(chat.getId(), chat);

        return responseMessage;
    }

    private void publicarEventoCliente(Integer chatId, Map<String, String> dados) {
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
