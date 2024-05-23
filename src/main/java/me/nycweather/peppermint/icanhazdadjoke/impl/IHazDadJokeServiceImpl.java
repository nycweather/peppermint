package me.nycweather.peppermint.icanhazdadjoke.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.icanhazdadjoke.config.WebClientConfig;
import me.nycweather.peppermint.icanhazdadjoke.model.DadJoke;
import me.nycweather.peppermint.icanhazdadjoke.service.IHazDadJokeService;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class IHazDadJokeServiceImpl implements IHazDadJokeService {

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    private final WebClientConfig webClientBuilder;
    private final ChatClient chatClient;

    public IHazDadJokeServiceImpl(WebClientConfig webClientBuilder, ChatClient chatClient) {
        this.webClientBuilder = webClientBuilder;
        this.chatClient = chatClient;
    }

    @Override
    public ResponseEntity<Map<String, String>> getDadJoke() {
        try {
            log.info("Fetching dad joke");
            String dadJokeRaw = webClientBuilder.webClientBuilder().build()
                    .get()
                    .uri("https://icanhazdadjoke.com/")
                    .headers(httpHeaders -> httpHeaders.set("Accept", "application/json"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            ObjectMapper objectMapper = new ObjectMapper();
            DadJoke dadJoke = objectMapper.readValue(dadJokeRaw, DadJoke.class);
            log.info("Dad joke fetched successfully, with joke: {} & status {}", dadJoke.getJoke(), dadJoke.getStatus());
            log.info("Calling openAI api, current model: {}", model);
            String response = chatClient.call("Explain this joke to me: " + dadJoke.getJoke());
            log.info("OpenAI api called successfully");
            return new ResponseEntity<>(Map.of("message", dadJoke.getJoke(), "explanation", response, "model", model), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting dad joke", e);
        }
        return new ResponseEntity<>(Map.of("message", "Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
