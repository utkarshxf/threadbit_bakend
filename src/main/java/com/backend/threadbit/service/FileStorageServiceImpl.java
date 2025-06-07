package com.backend.threadbit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Implementation of the FileStorageService interface
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public String saveImage(MultipartFile file, String filename) throws IOException {
        // Create the upload directory if it doesn't exist
        createUploadDirectoryIfNotExists();
        
        // Generate a unique filename if not provided
        if (filename == null || filename.isEmpty()) {
            filename = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        }
        
        // Save the file
        Path filePath = Paths.get(uploadDir, filename);
        Files.copy(file.getInputStream(), filePath);
        
        // Return the URL of the saved file
        return baseUrl + "/uploads/" + filename;
    }

    @Override
    public String saveImage(byte[] imageData, String filename) throws IOException {
        // Create the upload directory if it doesn't exist
        createUploadDirectoryIfNotExists();
        
        // Generate a unique filename if not provided
        if (filename == null || filename.isEmpty()) {
            filename = UUID.randomUUID().toString() + ".jpg";
        }
        
        // Save the file
        Path filePath = Paths.get(uploadDir, filename);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(imageData);
        }
        
        // Return the URL of the saved file
        return baseUrl + "/uploads/" + filename;
    }

    @Override
    public boolean deleteFile(String filename) {
        try {
            Path filePath = Paths.get(uploadDir, filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Create the upload directory if it doesn't exist
     */
    private void createUploadDirectoryIfNotExists() {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    /**
     * Get the file extension from a filename
     * 
     * @param filename the filename
     * @return the file extension (including the dot)
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty() || !filename.contains(".")) {
            return ".jpg"; // Default extension
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}