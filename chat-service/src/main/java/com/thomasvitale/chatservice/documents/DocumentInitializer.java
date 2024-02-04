package com.thomasvitale.chatservice.documents;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class DocumentInitializer {

    private static final Logger log = LoggerFactory.getLogger(DocumentInitializer.class);
    private final VectorStore vectorStore;

    @Value("classpath:documents/story1.md")
    Resource textFile1;

    @Value("classpath:documents/story2.txt")
    Resource textFile2;

    public DocumentInitializer(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void run() {
        List<Document> documents = new ArrayList<>();

        log.info("Loading .md files as Documents (dukes)");
        var textReader1 = new TextReader(textFile1);
        textReader1.getCustomMetadata().put("tenant", "dukes");
        textReader1.setCharset(Charset.defaultCharset());
        documents.addAll(textReader1.get());

        log.info("Loading .txt files as Documents (beans)");
        var textReader2 = new TextReader(textFile2);
        textReader2.getCustomMetadata().put("tenant", "beans");
        textReader2.setCharset(Charset.defaultCharset());
        documents.addAll(textReader2.get());

        log.info("Splitting text into chunks");
        var tokenizedDocuments = new TokenTextSplitter().apply(documents);

        log.info("Creating and storing Embeddings from Documents");
        vectorStore.add(tokenizedDocuments);
    }

}
