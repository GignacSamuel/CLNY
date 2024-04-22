package com.example.clny.controller;

import com.example.clny.dto.CommentDTO;
import com.example.clny.dto.ConversationDTO;
import com.example.clny.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/getConversations/{userId}")
    public ResponseEntity<List<ConversationDTO>> getConversations(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getConversations(userId));
    }

    @PostMapping("/createConversation")
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody ConversationDTO conversationDTO) throws Exception {
        return ResponseEntity.ok(messageService.createConversation(conversationDTO));
    }

}
