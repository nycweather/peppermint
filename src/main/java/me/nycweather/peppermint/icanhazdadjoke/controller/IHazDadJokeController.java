package me.nycweather.peppermint.icanhazdadjoke.controller;

import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.icanhazdadjoke.service.IHazDadJokeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequestMapping("/api/v1")
@RestController
public class IHazDadJokeController {

    private final IHazDadJokeService iHazDadJokeService;

    public IHazDadJokeController(IHazDadJokeService iHazDadJokeService) {
        this.iHazDadJokeService = iHazDadJokeService;
    }

    @GetMapping("/joke")
    public ResponseEntity<Map<String, String>> getJoke() {
        log.info("New request to /joke");
        return iHazDadJokeService.getDadJoke();
    }
}
