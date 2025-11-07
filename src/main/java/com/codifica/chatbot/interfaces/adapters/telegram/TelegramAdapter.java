package com.codifica.chatbot.interfaces.adapters.telegram;

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
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
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
    private final MessageAdapter messageAdapter;

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
        this.messageAdapter = new MessageAdapter();
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
        else if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        try {
            execute(AnswerCallbackQuery.builder().callbackQueryId(callbackQuery.getId()).build());
        } catch (TelegramApiException e) {
            logger.warn("Falha ao responder ao callbackQuery: {}", e.getMessage());
        }

        processUserInteraction(chatId, callbackData);
    }

    private void handleMessage(Message message) {
        long chatId = message.getChatId();
        Chat currentChat = getOrCreateChat(chatId);
        String userMessage;

        if (message.hasContact() && currentChat.getPassoAtual().equals("AGUARDANDO_TELEFONE_CLIENTE")) {
            String rawPhoneNumber = message.getContact().getPhoneNumber();
            String digitsOnly = rawPhoneNumber.replaceAll("\\D", "");

            if (digitsOnly.length() > 11) {
                userMessage = digitsOnly.substring(digitsOnly.length() - 11);
            } else {
                userMessage = digitsOnly;
            }

            sendAdaptedMessage(chatId, "Obrigado!", "REMOVE_KEYBOARD", null);
        }
        else if (message.hasText()) {
            userMessage = message.getText();

            // Lógica de reset com /start
            if (userMessage.equals("/start") && !currentChat.getPassoAtual().equals("INICIO")) {
                currentChat.setPassoAtual("INICIO");
                currentChat.setDadosContexto("{}");
                currentChat.setCliente(null);
                currentChat.setDataAtualizacao(LocalDateTime.now());
                updateChatUseCase.execute(currentChat.getId(), currentChat);
            }
        }
        else {
            return;
        }

        processUserInteraction(chatId, userMessage);
    }

    private void processUserInteraction(long chatId, String userMessage) {
        try {
            Chat currentChat = findChatByIdUseCase.execute(chatId)
                    .orElseThrow(() -> new IllegalStateException("Chat não encontrado para processamento"));

            String responseMessage = chatFlowService.processMessage(currentChat, userMessage);

            Chat updatedChat = findChatByIdUseCase.execute(chatId)
                    .orElseThrow(() -> new IllegalStateException("Chat desapareceu após processamento"));

            sendAdaptedMessage(chatId, responseMessage, updatedChat.getPassoAtual(), updatedChat.getDadosContexto());

        } catch (Exception e) {
            logger.error("Erro ao processar mensagem do Telegram para o chat {}: {}", chatId, e.getMessage(), e);
            sendAdaptedMessage(chatId, "Desculpe, ocorreu um erro inesperado. Tente novamente mais tarde.", "ERRO", null);
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
        try {
            Chat updatedChat = findChatByIdUseCase.execute(chatId)
                    .orElseThrow(() -> new IllegalStateException("Chat não encontrado para notificação"));
            sendAdaptedMessage(chatId, message, updatedChat.getPassoAtual(), updatedChat.getDadosContexto());
        } catch (Exception e) {
            logger.error("Falha ao enviar mensagem proativa para o chat {}: {}", chatId, e.getMessage(), e);
        }
    }

    private void sendAdaptedMessage(long chatId, String message, String nextStep, String dadosContexto) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);

        messageAdapter.adaptMessage(sendMessage, nextStep, message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Falha ao enviar mensagem adaptada para o chat {}: {}", chatId, e.getMessage(), e);
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
