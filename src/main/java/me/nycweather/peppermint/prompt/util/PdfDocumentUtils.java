package me.nycweather.peppermint.prompt.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PdfDocumentUtils {
    private final EmbeddingUtils embeddingUtils;

    public PdfDocumentUtils(EmbeddingUtils embeddingUtils) {
        this.embeddingUtils = embeddingUtils;
    }

    public List<String> extractDocumentsFromPdf(Resource pdfResource) {
        log.info("Extracting documents from pdf: {}", pdfResource.getFilename());
        PdfDocumentReaderConfig pdfDocumentReaderConfig = PdfDocumentReaderConfig.builder()
                .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder()
                        .withNumberOfBottomTextLinesToDelete(0)
                        .withNumberOfTopTextLinesToDelete(0)
                        .build())
                .withPagesPerDocument(1)
                .build();
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource, pdfDocumentReaderConfig);
        TokenTextSplitter textSplitter = new TokenTextSplitter();
        List<Document> docResourceList = textSplitter.apply(pdfReader.get());
        List<String> extractedList = docResourceList.stream()
                .map(Document::getContent)
                .toList();
        // apply embeddingUtils.sanitizeText(String s) to each element in extractedList
        return extractedList.stream()
                .map(embeddingUtils::sanitizeText)
                .toList();
    }


}
