package com.backend.threadbit.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service for handling file storage operations
 */
public interface FileStorageService {
    
    /**
     * Save an image file
     * 
     * @param file the image file to save
     * @param filename the name to save the file as
     * @return the URL of the saved image
     * @throws IOException if an I/O error occurs
     */
    String saveImage(MultipartFile file, String filename) throws IOException;
    
    /**
     * Save an image from byte array
     * 
     * @param imageData the image data as byte array
     * @param filename the name to save the file as
     * @return the URL of the saved image
     * @throws IOException if an I/O error occurs
     */
    String saveImage(byte[] imageData, String filename) throws IOException;
    
    /**
     * Delete a file
     * 
     * @param filename the name of the file to delete
     * @return true if the file was deleted, false otherwise
     */
    boolean deleteFile(String filename);
}