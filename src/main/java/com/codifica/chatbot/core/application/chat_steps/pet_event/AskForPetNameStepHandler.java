package com.codifica.chatbot.core.application.chat_steps.pet_event;

import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.chat.ConversationStep;
import com.codifica.chatbot.core.domain.chat.StepResponse;

public class AskForPetNameStepHandler implements ConversationStep {

    @Override
    public String getStepName() {
        return "AGUARDANDO_CADASTRO_PET";
    }

    @Override
    public StepResponse process(Chat chat, String userMessage) {
        String clienteNome = chat.getCliente() != null ? chat.getCliente().getNome() : "";
        String responseMessage = String.format(
                "Ótimo, %s! Seu cadastro foi concluído com sucesso ✅ Agora vamos cadastrar seu pet! Qual o nome dele?",
                clienteNome
        );
        return new StepResponse(responseMessage, "AGUARDANDO_NOME_PET");
    }
}
