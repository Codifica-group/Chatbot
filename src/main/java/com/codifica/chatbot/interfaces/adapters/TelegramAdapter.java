package com.codifica.chatbot.interfaces.adapters;

import com.codifica.chatbot.core.application.usecase.chat.CreateChatUseCase;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.infrastructure.chat.ChatFlowService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Profile("telegram")
public class TelegramAdapter extends TelegramLongPollingBot implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(TelegramAdapter.class);

    private final String botUsername;
    private final String botToken;
    private final ChatFlowService chatFlowService;
    private final CreateChatUseCase createChatUseCase;
    private final FindChatByIdUseCase findChatByIdUseCase;
    private final UpdateChatUseCase updateChatUseCase;

    public TelegramAdapter(
            @Value("${TELEGRAM_BOT_USERNAME}") String botUsername,
            @Value("${TELEGRAM_BOT_TOKEN}") String botToken,
            ChatFlowService chatFlowService,
            CreateChatUseCase createChatUseCase,
            FindChatByIdUseCase findChatByIdUseCase,
            UpdateChatUseCase updateChatUseCase) {
        super(botToken);
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.chatFlowService = chatFlowService;
        this.createChatUseCase = createChatUseCase;
        this.findChatByIdUseCase = findChatByIdUseCase;
        this.updateChatUseCase = updateChatUseCase;
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userMessage = update.getMessage().getText();
            long telegramChatId = update.getMessage().getChatId();

            try {
                Chat currentChat = getOrCreateChat(telegramChatId);
                String responseMessage;

                // Lógica de reset com /start
                if (userMessage.equals("/start") && !currentChat.getPassoAtual().equals("INICIO")) {
                    currentChat.setPassoAtual("INICIO");
                    currentChat.setDadosContexto("{}");
                    currentChat.setCliente(null); // Desvincula o cliente para recadastro
                    currentChat.setDataAtualizacao(LocalDateTime.now());
                    updateChatUseCase.execute(currentChat.getId(), currentChat);
                    // O ChatFlowService processará o "INICIO" e enviará a 1ª mensagem
                }

                responseMessage = chatFlowService.processMessage(currentChat, userMessage);

                sendMessage(telegramChatId, responseMessage);

            } catch (Exception e) {
                logger.error("Erro ao processar mensagem do Telegram para o chat {}: {}", telegramChatId, e.getMessage(), e);
                sendMessage(telegramChatId, "Desculpe, ocorreu um erro inesperado. Tente novamente mais tarde.");
            }
        }
    }

    private Chat getOrCreateChat(long telegramChatId) {
        Optional<Chat> chatOpt = findChatByIdUseCase.execute(telegramChatId);
        if (chatOpt.isPresent()) {
            return chatOpt.get();
        } else {
            Chat newChat = new Chat(telegramChatId, "INICIO", "{}", LocalDateTime.now(), null);
            createChatUseCase.execute(newChat);
            return newChat;
        }
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Falha ao enviar mensagem proativa para o chat {}: {}", chatId, e.getMessage(), e);
        }
    }

    @PostConstruct
    public void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            logger.info("Bot do Telegram registrado com sucesso: {}", this.botUsername);
        } catch (TelegramApiException e) {
            logger.error("Falha ao registrar o bot do Telegram", e);
        }
    }
}
