package com.thomasvitale.chatservice.chat;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ChatController {

    private final ChatService chatService;

    ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ai/doc/chat")
    String chatWithDocument(@RequestBody String input) {
        return chatService.chatWithDocument(input).getContent();
    }

}
