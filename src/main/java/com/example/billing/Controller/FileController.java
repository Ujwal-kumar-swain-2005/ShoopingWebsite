package com.example.billing.Controller;

import com.example.billing.Entity.FileEntity;
import com.example.billing.Service.File.FileUploadService;
import com.example.billing.io.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileUploadService.uploadFile(file);
            String fileId = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            FileUploadResponse response = fileUploadService.getFileInfo(fileId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) {
        try {
            FileEntity fileEntity = fileUploadService.getFileEntity(fileId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileEntity.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + fileEntity.getOriginalFilename() + "\"")
                    .body(fileEntity.getFileData());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
    }

    @GetMapping("/{fileId}/info")
    public ResponseEntity<FileUploadResponse> getFileInfo(@PathVariable String fileId) {
        try {
            FileUploadResponse response = fileUploadService.getFileInfo(fileId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) {
        try {
            String fileUrl = "http://localhost:8080/api/files/" + fileId;
            boolean deleted = fileUploadService.deleteFile(fileUrl);
            if (deleted) {
                return ResponseEntity.ok("File deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

