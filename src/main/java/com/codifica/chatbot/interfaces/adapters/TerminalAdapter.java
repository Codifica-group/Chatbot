package com.codifica.chatbot.interfaces.adapters;

import com.codifica.chatbot.config.DevDataInitializer;
import com.codifica.chatbot.core.application.usecase.chat.CreateChatUseCase;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.application.usecase.chat.ListChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.infrastructure.chat.ChatFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

@Component
@Profile("terminal")
public class TerminalAdapter implements CommandLineRunner {

    @Autowired
    private CreateChatUseCase createChatUseCase;

    @Autowired
    private FindChatByIdUseCase findChatByIdUseCase;

    @Autowired
    private ListChatUseCase listChatUseCase;

    @Autowired
    private ChatFlowService chatFlowService;

    @Autowired
    private DevDataInitializer devDataInitializer;

    private static final Set<String> WAITING_STEPS = Set.of(
            "AGUARDANDO_RESPOSTA_CADASTRO_CLIENTE",
            "AGUARDANDO_RESPOSTA_CADASTRO_PET",
            "AGUARDANDO_RESPOSTA_SOLICITACAO_PETSHOP",
            "AGUARDANDO_RESPOSTA_SOLICITACAO_API"
    );

    @Override
    public void run(String... args) throws Exception {
        devDataInitializer.initDatabase();
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Chatbot Terminal Iniciado (digite 'sair' para trocar de chat ou encerrar) ---");

        Chat currentChat = null;

        while (true) {
            if (currentChat == null) {
                System.out.println("\nSelecione um chat:");
                System.out.println("0 - Novo Chat");
                List<Chat> chats = listChatUseCase.execute();
                for (int i = 0; i < chats.size(); i++) {
                    System.out.printf("%d - Chat %d (Passo: %s)\n", i + 1, chats.get(i).getId(), chats.get(i).getPassoAtual());
                }
                System.out.println(chats.size() + 1 + " - Encerrar simulação");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 0) {
                    Chat chatInicial = new Chat(null, "INICIO", "{}", LocalDateTime.now(), null);
                    Map<String, Object> response = createChatUseCase.execute(chatInicial);
                    Integer chatId = (Integer) response.get("id");
                    currentChat = findChatByIdUseCase.execute(chatId).orElseThrow();
                    String chatbotResponse = chatFlowService.processMessage(currentChat, "");
                    System.out.println("Bot: " + chatbotResponse);
                } else if (choice > 0 && choice <= chats.size()) {
                    currentChat = chats.get(choice - 1);
                    System.out.println("Continuando chat " + currentChat.getId() + " no passo " + currentChat.getPassoAtual() + ".");
                } else {
                    break;
                }
            }

            if (WAITING_STEPS.contains(currentChat.getPassoAtual())) {
                currentChat = handleWaitingStep(currentChat);
                continue;
            }

            System.out.print("Você: ");
            String userInput = scanner.nextLine();

            if ("sair".equalsIgnoreCase(userInput)) {
                currentChat = null;
                continue;
            }

            String chatbotResponse = chatFlowService.processMessage(currentChat, userInput);
            System.out.println("Bot: " + chatbotResponse);
            currentChat = findChatByIdUseCase.execute(currentChat.getId()).orElseThrow();
        }

        scanner.close();
        System.out.println("--- Conversa Finalizada ---");
    }

    private Chat handleWaitingStep(Chat chat) throws InterruptedException {
        Chat currentChat = chat;
        System.out.println("\n[Simulação] Aguardando evento para o passo: " + currentChat.getPassoAtual());

        while (WAITING_STEPS.contains(currentChat.getPassoAtual())) {
            Thread.sleep(1000);
            currentChat = findChatByIdUseCase.execute(currentChat.getId()).orElseThrow();
        }

        System.out.println("[Simulação] Evento recebido! Continuando fluxo...");

        String chatbotResponse = chatFlowService.processMessage(currentChat, "");
        System.out.println("Bot: " + chatbotResponse);
        return findChatByIdUseCase.execute(currentChat.getId()).orElseThrow();
    }
}
