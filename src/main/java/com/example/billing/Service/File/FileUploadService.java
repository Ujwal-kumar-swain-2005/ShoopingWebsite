package com.example.billing.Service.File;

import com.example.billing.Entity.FileEntity;
import com.example.billing.Repository.FileRepository;
import com.example.billing.io.FileUploadResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FileUploadService implements FileUploadImpl {

    @Autowired
    private FileRepository fileRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // Extract file information
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.contains(".")) {
                throw new RuntimeException("Invalid file name");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileId = UUID.randomUUID().toString() + "." + fileExtension;
            String contentType = file.getContentType();
            Long fileSize = file.getSize();
            byte[] fileData = file.getBytes();

            // Validate file size (e.g., max 10MB)
            if (fileSize > 10 * 1024 * 1024) {
                throw new RuntimeException("File size exceeds maximum limit of 10MB");
            }

            // Create and save file entity
            FileEntity fileEntity = new FileEntity(
                    fileId, originalFilename, fileExtension,
                    contentType, fileSize, fileData
            );

            fileRepository.save(fileEntity);

            // Return the file URL
            return baseUrl + "/api/files/" + fileId;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String imgUrl) {
        try {
            // Extract file ID from URL
            String fileId = extractFileIdFromUrl(imgUrl);

            Optional<FileEntity> fileEntity = fileRepository.findByFileId(fileId);
            if (fileEntity.isPresent()) {
                fileRepository.deleteByFileId(fileId);
                return true;
            }
            return false;

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }
    }

    // Additional methods for file operations
    public FileUploadResponse getFileInfo(String fileId) {
        FileEntity fileEntity = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        return new FileUploadResponse(
                fileEntity.getFileId(),
                fileEntity.getOriginalFilename(),
                baseUrl + "/api/files/" + fileEntity.getFileId(),
                fileEntity.getContentType(),
                fileEntity.getFileSize(),
                fileEntity.getCreatedAt()
        );
    }

    public byte[] getFileData(String fileId) {
        FileEntity fileEntity = fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return fileEntity.getFileData();
    }

    public FileEntity getFileEntity(String fileId) {
        return fileRepository.findByFileId(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    private String extractFileIdFromUrl(String url) {
        // Extract file ID from URL like "http://localhost:8080/api/files/uuid.extension"
        if (url.contains("/api/files/")) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return url; // Assume it's already a file ID
    }
}
