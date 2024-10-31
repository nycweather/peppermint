package me.nycweather.peppermint.prompt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChatService {
    private final ChatClient chatClient;
    private final VectorStoreService vectorStoreService;

    @Value("classpath:/prompts/first-person-pov.st")
    private Resource localSearchSt;


    public ChatService(ChatClient chatClient,
                       VectorStoreService vectorStoreService) {
        this.chatClient = chatClient;
        this.vectorStoreService = vectorStoreService;
    }

    public String getAnswers(String question) {
        PromptTemplate promptTemplate = new PromptTemplate(localSearchSt);
        List<String> docs = (vectorStoreService.similaritySearch(Map.of("query", question, "topK", "2"))).content();
        Prompt prompt = promptTemplate.create(Map.of("input", question, "documents", docs));
        log.info("Prompt: {}", prompt);
        Generation response = chatClient.call(prompt).getResult();
        AssistantMessage assistantResponse = response.getOutput();
        return assistantResponse.getContent();
    }
}
