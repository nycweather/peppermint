package me.nycweather.peppermint.icanhazdadjoke.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.icanhazdadjoke.config.WebClientConfig;
import me.nycweather.peppermint.icanhazdadjoke.model.DadJoke;
import me.nycweather.peppermint.icanhazdadjoke.service.IHazDadJokeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class IHazDadJokeServiceImpl implements IHazDadJokeService {

    private final WebClientConfig webClientBuilder;

    public IHazDadJokeServiceImpl(WebClientConfig webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
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
            return new ResponseEntity<>(Map.of("message", dadJoke.getJoke()), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting dad joke", e);
        }
        return new ResponseEntity<>(Map.of("message", "Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
