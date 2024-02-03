package com.thomasvitale.chatservice.chat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.thomasvitale.chatservice.multitenancy.context.TenantContextHolder;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    ChatService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    AssistantMessage chatWithDocument(String message) {
        var systemPromptTemplate = new SystemPromptTemplate("""
                Answer questions given the context information below (DOCUMENTS section) and no prior knowledge,
                but act as if you knew this information innately. If the answer is not found in the DOCUMENTS section,
                simply state that you don't know the answer.

                DOCUMENTS:
                {documents}
                """);

        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest
                        .query(message)
                        .withFilterExpression("tenant == '%s'".formatted(TenantContextHolder.getTenantIdentifier()))
                        .withTopK(2));
        String documents = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining(System.lineSeparator()));

        Map<String,Object> model = Map.of("documents", documents);
        var systemMessage = systemPromptTemplate.createMessage(model);

        var userMessage = new UserMessage(message);
        var prompt = new Prompt(List.of(systemMessage, userMessage));

        var chatResponse = chatClient.call(prompt);
        return chatResponse.getResult().getOutput();
    }

}