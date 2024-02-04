package com.thomasvitale.chatservice.chat;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class DocumentChatController {

    private final DocumentChatService documentChatService;

    DocumentChatController(DocumentChatService documentChatService) {
        this.documentChatService = documentChatService;
    }

    @PostMapping("/ai/doc/chat")
    String chatWithDocument(@RequestBody String input) {
        return documentChatService.chatWithDocument(input).getContent();
    }

}
