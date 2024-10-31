package me.nycweather.peppermint.prompt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
public class RAGConfiguration {
    @Bean(name = "customVectorStore")
    public VectorStore customVectorStore(JdbcTemplate jdbcTemplate, EmbeddingClient embeddingClient) {
        log.info("Creating vector store");
        return new PgVectorStore(jdbcTemplate, embeddingClient);
    }
}
