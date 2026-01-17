package com.luizatasks.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileController {
    
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileController() {
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directory!", ex);
        }
    }
    
    @PostMapping("/upload")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Convert to base64 for database storage (works in ephemeral file systems like Render)
            byte[] fileBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(fileBytes);
            
            // Detect content type
            String contentType = file.getContentType();
            if (contentType == null) {
                contentType = "image/jpeg"; // Default
            }
            
            // Create data URL
            String dataUrl = "data:" + contentType + ";base64," + base64Image;
            
            Map<String, String> response = new HashMap<>();
            response.put("url", dataUrl);
            response.put("fileUrl", dataUrl); // Alias for compatibility
            
            return ResponseEntity.ok(response);
        } catch (IOException ex) {
            throw new RuntimeException("Could not process file. Please try again!", ex);
        }
    }
    
    @GetMapping("/{fileName:.+}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            
            // Security check: prevent directory traversal
            if (!filePath.startsWith(fileStorageLocation.normalize())) {
                return ResponseEntity.status(403).build();
            }
            
            Resource resource = new UrlResource(filePath.toUri());
            
            // Try to serve file from local storage (for backward compatibility)
            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, contentType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CACHE_CONTROL, "public, max-age=31536000")
                        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, OPTIONS")
                        .body(resource);
            }
            
            // File doesn't exist (common in ephemeral file systems like Render)
            // Images should now be stored as base64 data URLs in the database
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }
}

