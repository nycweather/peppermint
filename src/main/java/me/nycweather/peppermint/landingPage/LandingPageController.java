package me.nycweather.peppermint.landingPage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LandingPageController {

    @GetMapping
    public ResponseEntity<Map<String, String>> sayHello(){
        return new ResponseEntity<>(Map.of("message", "Hello, World!"), HttpStatus.OK);
    }
}
