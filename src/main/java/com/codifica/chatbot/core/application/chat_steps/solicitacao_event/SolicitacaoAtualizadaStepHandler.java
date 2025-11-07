package com.codifica.chatbot.core.application.chat_steps.solicitacao_event;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;
import com.codifica.chatbot.core.domain.events.solicitacao.SolicitacaoAtualizadaEvent;
import com.codifica.chatbot.core.domain.shared.DiaDaSemana;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.format.DateTimeFormatter;

public class SolicitacaoAtualizadaStepHandler implements ConversationStep {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public String getStepName() {
        return "SOLICITACAO_RESPONDIDA_PETSHOP";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        try {
            SolicitacaoAtualizadaEvent event = objectMapper.readValue(chat.getDadosContexto(), SolicitacaoAtualizadaEvent.class);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
            String diaDaSemana = DiaDaSemana.fromDate(event.getSolicitacao().getDataHoraInicio().toLocalDate());
            String dataFormatada = diaDaSemana + ", " + event.getSolicitacao().getDataHoraInicio().format(formatter);


            String valorTotal = String.format("%.2f", event.getSolicitacao().getValorTotal());
            String servicos = event.getSolicitacao().getServicos().stream().map(servico -> servico.getNome()).reduce((a, b) -> a + ", " + b).get();

            if (event.getAceito()) {
                String mensagem = "Boas notícias! O pet shop já respondeu sua solicitação 🎉\n\n";
                if (event.getDataInicioAlterada()) {
                    mensagem += "**Atenção:** A data do agendamento foi alterada.\n\n";
                }
                mensagem += "Detalhes do Agendamento:\n";
                mensagem += "- Pet: " + event.getSolicitacao().getPet().getNome() + "\n";
                mensagem += "- Serviços: " + servicos + "\n";
                mensagem += "- Horário: " + dataFormatada + "\n";
                mensagem += "- Valor Total: R$ " + valorTotal + "\n\n";
                mensagem += "Por favor, confirme o agendamento:\n";
                mensagem += "1. Confirmar\n";
                mensagem += "2. Recusar";
                return new StepResponse(mensagem, "AGUARDANDO_RESPOSTA_SOLICITACAO_CLIENTE");
            } else {
                chat.setDadosContexto("{}");
                return new StepResponse("Infelizmente, o petshop não pôde aceitar sua solicitação de agendamento no momento 😔", "IDLE");
            }
        } catch (Exception e) {
            return new StepResponse("Ocorreu um erro ao processar a resposta do petshop. Tente novamente.", getStepName());
        }
    }
}
