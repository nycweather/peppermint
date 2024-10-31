package me.nycweather.peppermint.prompt.service;

import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.prompt.dto.PromptDtoMapper;
import me.nycweather.peppermint.prompt.dto.VectorStoreResponseDTO;
import me.nycweather.peppermint.prompt.model.DocumentModel;
import me.nycweather.peppermint.prompt.model.VectorStoreModel;
import me.nycweather.peppermint.prompt.repository.DocumentRepository;
import me.nycweather.peppermint.prompt.repository.VectorStoreRepository;
import me.nycweather.peppermint.prompt.util.EmbeddingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class VectorStoreService {

    private final VectorStoreRepository vectorStoreRepository;
    private final DocumentService documentService;
    private final DocumentRepository documentRepository;
    private final EmbeddingUtils embeddingUtils;
    private final PromptDtoMapper promptDtoMapper;
    private final RawQueryService rawQueryService;

    public VectorStoreService(VectorStoreRepository vectorStoreRepository,
                              DocumentRepository documentRepository,
                              EmbeddingUtils embeddingUtils,
                              DocumentService documentService,
                              PromptDtoMapper promptDtoMapper,
                              RawQueryService rawQueryService) {
        this.vectorStoreRepository = vectorStoreRepository;
        this.documentRepository = documentRepository;
        this.embeddingUtils = embeddingUtils;
        this.documentService = documentService;
        this.promptDtoMapper = promptDtoMapper;
        this.rawQueryService = rawQueryService;
    }

    @Transactional
    public void insertDocument(Map<String, String> requestBody) {
        String documentName = requestBody.get("documentName");
        String content = requestBody.get("content");
        if (documentName == null || content == null) {
            log.error("Invalid request body: documentName or content is missing");
            return;
        }
        log.info("Inserting document: {}", documentName);
        // Check if document already exists
        DocumentModel documentModel = documentRepository.findByDocumentName(documentName);
        if (documentModel != null) {
            log.info("Document already exists: {}", documentModel);
        } else {
            documentModel = documentService.processDocument(documentName);
            documentRepository.save(documentModel);
            // Insert document into vector store
            List<String> embeddingCandidates = embeddingUtils.splitTextForEmbedding(content);
            List<VectorStoreModel> vectorStoreModels = embeddingCandidates.stream()
                    .map(candidate -> VectorStoreModel.builder()
                            .documentName(documentName)
                            .content(candidate)
                            .createdAt(LocalDateTime.now())
                            .embedding(embeddingUtils.getEmbedding(candidate))
                            .build())
                    .toList();
            vectorStoreRepository.saveAll(vectorStoreModels);
            log.info("Document inserted: {}", documentModel);
        }


    }

    public VectorStoreResponseDTO similaritySearch(Map<String, String> requestBody) {
        String text = requestBody.get("query");
        int topK = Integer.parseInt(requestBody.get("topK"));
        if (text == null) {
            log.error("Invalid request body: query is missing");
            return null;
        }
        Double[] queryEmbedding = embeddingUtils.getEmbedding(text);
//        log.info("Query embeddings '{}'", Arrays.toString(queryEmbedding));
        String queryEmbeddingString = embeddingUtils.convertToPostgresArrayString(queryEmbedding);
//        log.info("Query embeddings '{}'", queryEmbeddingString);
        List<VectorStoreModel> result = rawQueryService.similaritySearch(queryEmbeddingString, topK);
        return promptDtoMapper.vectorStoreModelToDto(result);
    }

}
