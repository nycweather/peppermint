package me.nycweather.peppermint.prompt.util;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EmbeddingUtils {
    private final EmbeddingClient embeddingClient;
    private static final String PARAGRAPH_SPLIT_REGEX = "(?m)(?=^\\s{4})";

    public EmbeddingUtils(EmbeddingClient embeddingClient) {
        this.embeddingClient = embeddingClient;
    }

    public Double[] getEmbedding(String text) {
        text = text.replaceAll("\\s{5,}", " ");
        List<Double> embeddingList = embeddingClient.embed(text);
        return embeddingList.toArray(new Double[embeddingList.size()]);
    }

    public List<String> splitTextForEmbedding(String text) {
        text = sanitizeText(text);
        return Arrays.stream(text.split(PARAGRAPH_SPLIT_REGEX))
                .map(String::trim)
                .filter(chunk -> !chunk.isEmpty())
                .toList();
    }

    public String convertToPostgresArrayString(Double[] vector) {
        return "[" + Stream.of(vector)
                .map(String::valueOf)
                .collect(Collectors.joining(",")) + "]";
    }

    public String sanitizeText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        // Replace multiple spaces and new lines with a single space
        return text.replaceAll("[\\s\\n\\r]+", " ").trim();
    }

}
