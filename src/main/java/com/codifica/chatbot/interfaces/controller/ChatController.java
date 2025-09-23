package com.codifica.chatbot.interfaces.controller;

import com.codifica.chatbot.core.application.usecase.chat.CreateChatUseCase;
import com.codifica.chatbot.core.application.usecase.chat.DeleteChatUseCase;
import com.codifica.chatbot.core.application.usecase.chat.FindChatByIdUseCase;
import com.codifica.chatbot.core.application.usecase.chat.ListChatUseCase;
import com.codifica.chatbot.core.application.usecase.chat.UpdateChatUseCase;
import com.codifica.chatbot.interfaces.dto.ChatDTO;
import com.codifica.chatbot.interfaces.mappers.ChatDtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ListChatUseCase listChatUseCase;
    private final CreateChatUseCase createChatUseCase;
    private final UpdateChatUseCase updateChatUseCase;
    private final FindChatByIdUseCase findChatByIdUseCase;
    private final DeleteChatUseCase deleteChatUseCase;
    private final ChatDtoMapper chatDtoMapper;

    public ChatController(ListChatUseCase listChatUseCase,
                          CreateChatUseCase createChatUseCase,
                          UpdateChatUseCase updateChatUseCase,
                          FindChatByIdUseCase findChatByIdUseCase,
                          DeleteChatUseCase deleteChatUseCase,
                          ChatDtoMapper chatDtoMapper) {
        this.listChatUseCase = listChatUseCase;
        this.createChatUseCase = createChatUseCase;
        this.updateChatUseCase = updateChatUseCase;
        this.findChatByIdUseCase = findChatByIdUseCase;
        this.deleteChatUseCase = deleteChatUseCase;
        this.chatDtoMapper = chatDtoMapper;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody ChatDTO chatDTO) {
        Map<String, Object> response = createChatUseCase.execute(chatDtoMapper.toDomain(chatDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ChatDTO>> listAll() {
        List<ChatDTO> chats = listChatUseCase.execute().stream()
                .map(chatDtoMapper::toDto)
                .collect(Collectors.toList());
        return chats.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(chats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatDTO> findById(@PathVariable Integer id) {
        return findChatByIdUseCase.execute(id)
                .map(chat -> ResponseEntity.ok(chatDtoMapper.toDto(chat)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Integer id, @RequestBody ChatDTO chatDTO) {
        Map<String, Object> response = updateChatUseCase.execute(id, chatDtoMapper.toDomain(chatDTO));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        deleteChatUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
