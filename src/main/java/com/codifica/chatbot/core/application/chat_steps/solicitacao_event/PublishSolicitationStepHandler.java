package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.application.ports.out.SolicitacaoEventPublisherPort;
import com.codifica.chatbot.core.application.util.ValidationUtil;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.disponibilidade.Disponibilidade;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoParaCadastrarEvent;
import com.codifica.chatbot.core.domain.servico.Servico;
import com.codifica.chatbot.core.domain.shared.Dia;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PublishSolicitationStepHandler implements ConversationStep {

    private final SolicitacaoEventPublisherPort solicitacaoEventPublisherPort;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Logger logger = LoggerFactory.getLogger(PublishSolicitationStepHandler.class);

    public PublishSolicitationStepHandler(SolicitacaoEventPublisherPort solicitacaoEventPublisherPort) {
        this.solicitacaoEventPublisherPort = solicitacaoEventPublisherPort;
    }

    @Override
    public String getStepName() {
        return "AGUARDANDO_ESCOLHA_SERVICOS";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            Map<String, Object> context = objectMapper.readValue(chat.getDadosContexto(), new TypeReference<>() {});
            List<Servico> servicosList = objectMapper.convertValue(context.get("listaServicos"), new TypeReference<List<Servico>>() {});
            List<Disponibilidade> disponibilidade = objectMapper.convertValue(context.get("disponibilidade"), new TypeReference<List<Disponibilidade>>() {});
            Integer diaEscolhidoIndex = objectMapper.convertValue(context.get("diaEscolhido"), Integer.class);

            String validationError = ValidationUtil.validateServiceChoice(userMessage, servicosList.size());
            if (validationError != null) {
                return new StepResponse(validationError, getStepName());
            }

            List<Integer> servicosIds = Arrays.stream(userMessage.split(","))
                    .map(s -> Integer.parseInt(s.trim()) - 1)
                    .map(index -> servicosList.get(index).getId())
                    .collect(Collectors.toList());

            Integer petId = (Integer) context.get("petId");
            LocalDate chosenDayObject = disponibilidade.get(diaEscolhidoIndex).getDia();
            String chosenTimeString = (String) context.get("horarioEscolhido");

            SolicitacaoParaCadastrarEvent event = new SolicitacaoParaCadastrarEvent(
                    chat.getId(),
                    petId,
                    servicosIds,
                    chosenDayObject.atTime(LocalTime.parse(chosenTimeString)),
                    null,
                    "AGUARDANDO_RESPOSTA_PETSHOP"
            );

            solicitacaoEventPublisherPort.publishSolicitacaoParaCadastrar(event);
            chat.setDadosContexto("{}");

            return new StepResponse("Sua solicitação foi enviada com sucesso! Em breve você receberá a confirmação.", "AGUARDANDO_RESPOSTA_SOLICITACAO_PETSHOP");

        } catch (Exception e) {
            logger.error("FALHA: Erro ao processar escolha dos serviços: ", e);
            return new StepResponse("Ocorreu um erro ao processar os serviços. Tente novamente.", getStepName());
        }
    }
}
