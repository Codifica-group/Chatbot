package com.codifica.chatbot.interfaces.adapters;

import com.codifica.chatbot.core.application.usecase.chat.CreateChatUseCase;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.infrastructure.chat.ChatFlowService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

@Component
@Profile("terminal")
public class TerminalAdapter implements CommandLineRunner {

    private final CreateChatUseCase createChatUseCase;
    private final FindChatByIdUseCase findChatByIdUseCase;
    private final ChatFlowService chatFlowService;

    public TerminalAdapter(CreateChatUseCase createChatUseCase, FindChatByIdUseCase findChatByIdUseCase, ChatFlowService chatFlowService) {
        this.createChatUseCase = createChatUseCase;
        this.findChatByIdUseCase = findChatByIdUseCase;
        this.chatFlowService = chatFlowService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Chatbot Terminal Iniciado (digite 'sair' para encerrar) ---");

        Chat chatInicial = new Chat(null, "INICIO", "{}", LocalDateTime.now(), null);
        Map<String, Object> response = createChatUseCase.execute(chatInicial);
        Integer chatId = (Integer) response.get("id");

        Chat currentChat = findChatByIdUseCase.execute(chatId).orElseThrow();
        String chatbotResponse = chatFlowService.processMessage(currentChat, "");
        System.out.println("Bot: " + chatbotResponse);

        while (true) {
            System.out.print("Você: ");
            String userInput = scanner.nextLine();

            if ("sair".equalsIgnoreCase(userInput)) {
                break;
            }

            currentChat = findChatByIdUseCase.execute(chatId).orElseThrow();
            chatbotResponse = chatFlowService.processMessage(currentChat, userInput);
            System.out.println("Bot: " + chatbotResponse);

            if ("AGUARDANDO_RESPOSTA_CADASTRO_CLIENTE".equals(currentChat.getPassoAtual())) {
                System.out.println("\n[Simulação] O fluxo de cadastro de cliente foi iniciado. O chatbot agora aguarda a resposta do backend.");
                break;
            }
        }

        scanner.close();
        System.out.println("--- Conversa Finalizada ---");
    }
}
