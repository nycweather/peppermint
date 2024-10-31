package me.nycweather.peppermint.prompt.component;


import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.prompt.model.DocumentModel;
import me.nycweather.peppermint.prompt.model.VectorStoreModel;
import me.nycweather.peppermint.prompt.repository.DocumentRepository;
import me.nycweather.peppermint.prompt.repository.VectorStoreRepository;
import me.nycweather.peppermint.prompt.service.DocumentService;
import me.nycweather.peppermint.prompt.util.EmbeddingUtils;
import me.nycweather.peppermint.prompt.util.PdfDocumentUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class LocalDocs {

    @Value("${upload.path}")
    private String UPLOAD_PATH;

    private final DocumentRepository documentRepository;
    private final DocumentService documentService;
    private final EmbeddingUtils embeddingUtils;
    private final VectorStoreRepository vectorStoreRepository;
    private final PdfDocumentUtils pdfDocumentUtils;
    private final ResourcePatternResolver resourcePatternResolver;


    public LocalDocs(DocumentRepository documentRepository,
                     DocumentService documentService,
                     EmbeddingUtils embeddingUtils,
                     VectorStoreRepository vectorStoreRepository,
                     PdfDocumentUtils pdfDocumentUtils,
                     ResourcePatternResolver resourcePatternResolver) {
        this.documentRepository = documentRepository;
        this.documentService = documentService;
        this.embeddingUtils = embeddingUtils;
        this.vectorStoreRepository = vectorStoreRepository;
        this.pdfDocumentUtils = pdfDocumentUtils;
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @Bean
    public void loadLocalDocs() {
        try {
            Resource[] resources = resourcePatternResolver.getResources("file:" + UPLOAD_PATH + "/*");
//            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (Resource resource : resources) {
//                executor.submit(() -> processDocumentResource(resource));
                processDocumentResource(resource);
            }
//            }
        } catch (Exception e) {
            log.error("Error loading local documents", e);
        }
    }

    protected void processDocumentResource(Resource resource) {
        try {
            String filename = resource.getFilename();
            if (filename == null) {
                log.warn("Filename is null for resource: {}", resource);
                return;
            }
            DocumentModel doc = documentRepository.findByDocumentName(filename);
            if (doc != null) {
                log.info("Document: {} already exists in the database...", filename);
            } else {
                log.info("Document: {} does not exist in the database, adding to database using Thread: {}", filename, Thread.currentThread().threadId());
                DocumentModel documentModel = documentService.processDocument(filename);
                documentRepository.save(documentModel);
                List<String> embeddingCandidates = pdfDocumentUtils.extractDocumentsFromPdf(resource);
                List<VectorStoreModel> vectorStoreModels = embeddingCandidates.parallelStream()
                        .map(candidate -> VectorStoreModel.builder()
                                .documentName(documentModel.getDocumentName())
                                .content(candidate)
                                .createdAt(LocalDateTime.now())
                                .embedding(embeddingUtils.getEmbedding(candidate))
                                .build())
                        .toList();
                vectorStoreRepository.saveAll(vectorStoreModels);
            }
        } catch (Exception e) {
            log.error("Error processing document: {}", resource, e);
        }
    }
}