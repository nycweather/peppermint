package me.nycweather.peppermint.prompt.service;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import me.nycweather.peppermint.prompt.model.VectorStoreModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

// https://github.com/spring-projects/spring-ai/blob/2306db4a30aaafed27667539e99db29c4d6b6ea9/vector-stores/spring-ai-pgvector-store/src/main/java/org/springframework/ai/vectorstore/PgVectorStore.java
@Service
public class RawQueryService {

    private final JdbcTemplate jdbcTemplate;

    public RawQueryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<VectorStoreModel> similaritySearch(String vector, int topK) {
        String query = """
                SELECT * FROM vector_storage vs ORDER BY embedding <=> '%s' LIMIT %d
                """.formatted(vector, topK);
        return getVectorStoreModels(query);
    }

    public List<VectorStoreModel> similaritySearch(String documentName, String vector, int topK) {
        String query = """
                SELECT * FROM vector_storage vs where document_name = '%s' ORDER BY embedding <=> '%s' LIMIT %d
                """.formatted(documentName, vector, topK);
        return getVectorStoreModels(query);
    }

    @NotNull
    private List<VectorStoreModel> getVectorStoreModels(String query) {
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            VectorStoreModel vectorStoreModel = new VectorStoreModel();
            vectorStoreModel.setId(rs.getLong("id"));
            vectorStoreModel.setDocumentName(rs.getString("document_name"));
            vectorStoreModel.setContent(rs.getString("content"));
            return vectorStoreModel;
        });
    }
}
