package com.digitaltimecapsule.digitaltimecapsule.controller;

import com.digitaltimecapsule.digitaltimecapsule.service.EncryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Value("${upload.dir}")
    private String uploadDir;

    private final EncryptionService encryptionService;

    public FileController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Path encryptedPath = Paths.get(uploadDir, filename);
        Path tempPath = Paths.get(uploadDir, "temp_" + filename);

        try {
            // Decrypt the file
            encryptionService.decryptFile(encryptedPath, tempPath);

            // Read the decrypted content
            byte[] content = Files.readAllBytes(tempPath);

            // Clean up the temporary file
            Files.delete(tempPath);

            // Determine content type
            String contentType = determineContentType(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(new ByteArrayResource(content));
        } catch (Exception e) {
            throw new IOException("Failed to serve file: " + filename, e);
        }
    }

    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "mp4" -> "video/mp4";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }
} 