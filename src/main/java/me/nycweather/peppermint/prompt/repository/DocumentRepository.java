package me.nycweather.peppermint.prompt.repository;

import me.nycweather.peppermint.prompt.model.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentModel, String> {
    DocumentModel findByDocumentName(String documentName);
}
