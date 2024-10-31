package me.nycweather.peppermint.prompt.service;

import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.prompt.repository.DocumentRepository;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
public class Orchestrator {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final DocumentService documentService;
    private final DocumentRepository documentRepository;
    private final VectorStoreService vectorStoreService;
    private final ChatService chatService;
    private final UploadService uploadService;


    public Orchestrator(ChatClient chatClient,
                        @Qualifier("customVectorStore") VectorStore vectorStore,
                        DocumentService documentService,
                        DocumentRepository documentRepository,
                        VectorStoreService vectorStoreService,
                        ChatService chatService,
                        UploadService uploadService
    ) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.documentService = documentService;
        this.documentRepository = documentRepository;
        this.vectorStoreService = vectorStoreService;
        this.chatService = chatService;
        this.uploadService = uploadService;
    }

    public ResponseEntity<Object> similaritySearch(Map<String, String> requestBody) {
        return new ResponseEntity<>(vectorStoreService.similaritySearch(requestBody), HttpStatus.OK);
    }

    public ResponseEntity<Object> insertDocument(Map<String, String> requestBody) {
        vectorStoreService.insertDocument(requestBody);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Object> getAnswers(String question) {
        return new ResponseEntity<>(chatService.getAnswers(question), HttpStatus.OK);
    }

    public ResponseEntity<Object> uploadDocument(MultipartFile file) {
        return uploadService.uploadDocument(file);
    }

}
