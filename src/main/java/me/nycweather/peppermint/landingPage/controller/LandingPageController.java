package me.nycweather.peppermint.landingPage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class LandingPageController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> sayHello() {
        log.info("New request to /hello");
        return new ResponseEntity<>(Map.of("message", "Hello, World!"), HttpStatus.OK);
    }
}
