package com.thomasvitale.chatservice.chat;

import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleChatController {

    private final ChatClient chatClient;

    public SimpleChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/ai/simple/chat")
    String chat(@RequestBody String message) {
        return chatClient.call(message);
    }

}
