package me.nycweather.peppermint.prompt.controller;

import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.prompt.service.Orchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/prompt")
public class PromptController {

    private final Orchestrator orchestrator;

    public PromptController(Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @GetMapping("/similarity-search")
    public ResponseEntity<Object> getNumberOfDocument(@RequestBody Map<String, String> requestBody) {
        log.info("Received new request for /api/v1/prompt/similarity-search");
        return orchestrator.similaritySearch(requestBody);
    }

    @PostMapping("/upload-document")
    public ResponseEntity<Object> uploadDocument(@RequestBody Map<String, String> requestBody) {
        log.info("Received new request for /api/v1/prompt/upload-document");
        return orchestrator.insertDocument(requestBody);
    }

    @GetMapping("/questions")
    public ResponseEntity<Object> getAnswers(@RequestParam String question) {
        log.info("Received new request for /api/v1/prompt/questions");
        return orchestrator.getAnswers(question);
    }

    @PostMapping("/documents")
    public ResponseEntity<Object> getDocuments(@RequestParam("file") MultipartFile file) {
        log.info("Received new request for /api/v1/prompt/documents");
        return orchestrator.uploadDocument(file);
    }
}
