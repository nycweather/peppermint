package me.nycweather.peppermint.prompt.service;

import lombok.extern.slf4j.Slf4j;
import me.nycweather.peppermint.prompt.component.LocalDocs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class UploadService {
    private final LocalDocs localDocs;

    public UploadService(LocalDocs localDocs) {
        this.localDocs = localDocs;
    }

    @Value("${upload.path}")
    private String UPLOAD_PATH;

    public ResponseEntity<Object> uploadDocument(MultipartFile file) {
        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String filePath = UPLOAD_PATH + file.getOriginalFilename();

        log.info("Uploading file: {}, Size: {}", file.getOriginalFilename(), file.getSize());
        try {
            file.transferTo(new File(filePath));
            localDocs.loadLocalDocs();
            return new ResponseEntity<>("uploadDocument", HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error uploading file", e);
            return new ResponseEntity<>("Error uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    private Response loadAsResource(MultipartFile file) {
//
//    }
}
