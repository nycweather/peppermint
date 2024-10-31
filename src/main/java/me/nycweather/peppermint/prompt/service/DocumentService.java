package me.nycweather.peppermint.prompt.service;

import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.prompt.model.DocumentModel;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DocumentService {

    public DocumentModel processDocument(String documentName) {
        return DocumentModel.builder()
                .documentName(documentName)
                .build();
    }
}
