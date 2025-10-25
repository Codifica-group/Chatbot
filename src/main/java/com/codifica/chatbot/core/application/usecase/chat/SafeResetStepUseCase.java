package com.codifica.chatbot.core.application.usecase.chat;

import com.codifica.chatbot.core.domain.chat.Chat;

import java.util.Set;

public class SafeResetStepUseCase {

    private static final Set<String> PET_REGISTRATION_STEPS = Set.of(
            "AGUARDANDO_CADASTRO_PET",
            "AGUARDANDO_NOME_PET",
            "AGUARDANDO_RACA_PET",
            "AGUARDANDO_ESCOLHA_RACA_SUGESTAO",
            "AGUARDANDO_RESPOSTA_CADASTRO_PET"
    );

    /**
     * Determina o passo "seguro" retorno em caso de erro.
     * A lógica é:
     * 1. Se o cliente não está cadastrado (chat.getCliente() == null), volta para o início.
     * 2. Se o cliente está cadastrado, mas o erro ocorreu durante o cadastro do pet, volta para o início do cadastro do pet.
     * 3. Se o cliente está cadastrado e o erro ocorreu fora do fluxo de cadastro de pet (ex: agendamento), volta para IDLE.
     */
    public static String execute(Chat chat) {
        if (chat.getCliente() == null) {
            return "INICIO";
        }

        if (PET_REGISTRATION_STEPS.contains(chat.getPassoAtual())) {
            return "AGUARDANDO_CADASTRO_PET";
        }

        return "IDLE";
    }
}
