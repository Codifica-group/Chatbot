package com.codifica.chatbot.interfaces.adapters;

import com.codifica.chatbot.core.application.services.ChatService;
import com.codifica.chatbot.core.application.usecase.chat.CreateChatUseCase;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
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
    private final ChatService chatService;

    public TerminalAdapter(CreateChatUseCase createChatUseCase, FindChatByIdUseCase findChatByIdUseCase, ChatService chatService) {
        this.createChatUseCase = createChatUseCase;
        this.findChatByIdUseCase = findChatByIdUseCase;
        this.chatService = chatService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Chatbot Terminal Iniciado (digite 'sair' para encerrar) ---");

        Chat chatInicial = new Chat(null, "INICIO", "{}", LocalDateTime.now(), null);
        Map<String, Object> response = createChatUseCase.execute(chatInicial);
        Integer chatId = (Integer) response.get("id");

        Chat currentChat = findChatByIdUseCase.execute(chatId).orElseThrow();
        String chatbotResponse = chatService.processMessage(currentChat, "");
        System.out.println("Bot: " + chatbotResponse);

        while (true) {
            System.out.print("Você: ");
            String userInput = scanner.nextLine();

            if ("sair".equalsIgnoreCase(userInput)) {
                break;
            }

            currentChat = findChatByIdUseCase.execute(chatId).orElseThrow();
            chatbotResponse = chatService.processMessage(currentChat, userInput);
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
