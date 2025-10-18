package com.codifica.chatbot.core.application.chat_steps;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;

public class IdleStepHandler implements ConversationStep {

    @Override
    public String getStepName() {
        return "IDLE";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        chat.setDadosContexto("{}");

        String nome = chat.getCliente() != null ? chat.getCliente().getNome() : "";
        String responseMessage = "Olá " + nome;
        return new StepResponse(responseMessage, "AGUARDANDO_AGENDAMENTO");
    }
}
