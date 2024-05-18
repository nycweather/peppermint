package me.nycweather.peppermint.landingPage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class LandingPageController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> sayHello() {
        return new ResponseEntity<>(Map.of("message", "Hello, World!"), HttpStatus.OK);
    }
}
