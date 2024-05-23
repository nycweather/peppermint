package me.nycweather.peppermint.icanhazdadjoke.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IHazDadJokeService {
    ResponseEntity<Map<String, String>> getDadJoke();
}
