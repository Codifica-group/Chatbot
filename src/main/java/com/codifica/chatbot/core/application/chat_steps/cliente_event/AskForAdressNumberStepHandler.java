package com.codifica.chatbot.core.application.chat_steps.cliente_event;

import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.infrastructure.services.MainBackendService;
import com.codifica.chatbot.interfaces.dto.CepDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Optional;

public class AskForAdressNumberStepHandler implements ConversationStep {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MainBackendService mainBackendService;

    public AskForAdressNumberStepHandler(MainBackendService mainBackendService) {
        this.mainBackendService = mainBackendService;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_CEP_CLIENTE";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        String validationError = ValidationUtil.validateCep(userMessage);
        if (validationError != null) {
            return new StepResponse(validationError, getStepName());
        }

        Optional<CepDTO> cepResult = mainBackendService.findEnderecoByCep(userMessage);

        if (cepResult.isEmpty()) {
            return new StepResponse("Desculpe, não consegui encontrar este CEP 🗺️ Por favor, verifique o número e digite novamente.", getStepName());
        }

        try {
            CepDTO endereco = cepResult.get();
            Map<String, String> dadosContexto = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});

            dadosContexto.put("cep", endereco.getCep().replaceAll("\\D", ""));
            dadosContexto.put("rua", endereco.getLogradouro());
            dadosContexto.put("bairro", endereco.getBairro());
            dadosContexto.put("cidade", endereco.getLocalidade());

            chat.setDadosContexto(objectMapper.writeValueAsString(dadosContexto));

            String responseMessage = "Entendi! Qual o número do seu endereço?";
            return new StepResponse(responseMessage, "AGUARDANDO_NUMERO_ENDERECO_CLIENTE");
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar seu cep. Tente novamente.", getStepName());
        }
    }
}
