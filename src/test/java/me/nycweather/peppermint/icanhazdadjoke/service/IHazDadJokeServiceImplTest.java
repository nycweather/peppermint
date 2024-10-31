package me.nycweather.peppermint.icanhazdadjoke.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.nycweather.peppermint.icanhazdadjoke.config.WebClientConfig;
import me.nycweather.peppermint.icanhazdadjoke.model.DadJoke;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IHazDadJokeServiceImplTest {

    @Mock
    private WebClientConfig webClientConfig;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private IHazDadJokeService iHazDadJokeServiceImpl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        when(webClientConfig.webClientBuilder()).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void testGetDadJokeSuccess() throws Exception {
        // Mock the JSON response
        String jokeJson = "{\"id\":\"123\",\"joke\":\"This is a dad joke\",\"status\":200}";
        DadJoke dadJoke = objectMapper.readValue(jokeJson, DadJoke.class);

        // Mock the WebClient behavior
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(jokeJson));

        ResponseEntity<Map<String, String>> response = iHazDadJokeServiceImpl.getDadJoke();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("This is a dad joke", response.getBody().get("message"));

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("https://icanhazdadjoke.com/");
        verify(requestHeadersSpec, times(1)).headers(any());
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }

    @Test
    public void testGetDadJokeFailure() {
        // Mock the WebClient behavior to simulate an error
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new RuntimeException("Error")));

        ResponseEntity<Map<String, String>> response = iHazDadJokeServiceImpl.getDadJoke();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody().get("message"));

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("https://icanhazdadjoke.com/");
        verify(requestHeadersSpec, times(1)).headers(any());
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }
}
