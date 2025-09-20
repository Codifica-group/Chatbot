package com.codifica.chatbot.interfaces.controller;

import com.codifica.chatbot.core.application.ports.in.ChatEventPort;
import com.codifica.chatbot.core.application.ports.out.ClienteEventPublisherPort;
import com.codifica.chatbot.core.application.usecase.ListChatUseCase;
import com.codifica.chatbot.core.domain.chat.Chat;
import com.codifica.chatbot.core.domain.events.cliente.ClienteParaCadastrarEvent;
import com.codifica.chatbot.interfaces.dto.ChatDTO;
import com.codifica.chatbot.interfaces.mappers.ChatDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test/events")
public class TestEventController {

    private final ClienteEventPublisherPort clienteEventPublisherPort;
    private final ChatEventPort chatEventPort;
    private final ListChatUseCase listChatUseCase;
    private final ChatDtoMapper chatDtoMapper;

    public TestEventController(ClienteEventPublisherPort clienteEventPublisherPort, ChatEventPort chatEventPort, ListChatUseCase listChatUseCase, ChatDtoMapper chatDtoMapper) {
        this.clienteEventPublisherPort = clienteEventPublisherPort;
        this.chatEventPort = chatEventPort;
        this.listChatUseCase = listChatUseCase;
        this.chatDtoMapper = chatDtoMapper;
    }

    @PostMapping("/cliente-para-cadastrar")
    public ResponseEntity<String> dispararEventoCliente(@RequestBody ClienteParaCadastrarEvent event) {
        Chat chat = new Chat();
        chat.setId(event.getChatId());
        chat.setPassoAtual("AGUARDANDO_CADASTRO_DE_CLIENTE");
        chat.setDataAtualizacao(LocalDateTime.now());
        chat.setDadosContexto("{\"evento\": \"ClienteParaCadastrarEvent\"}");

        chatEventPort.save(chat);
        clienteEventPublisherPort.publishClienteParaCadastrar(event);
        return ResponseEntity.ok("Chat salvo e evento de cadastro de cliente publicado com sucesso.");
    }

    @GetMapping("/chats")
    public ResponseEntity<List<ChatDTO>> listChats() {
        List<ChatDTO> chats = listChatUseCase.execute().stream()
                .map(chatDtoMapper::toDto)
                .collect(Collectors.toList());
        return chats.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(chats);
    }
}
