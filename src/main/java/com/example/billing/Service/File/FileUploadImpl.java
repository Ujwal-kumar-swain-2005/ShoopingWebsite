package com.example.billing.Service.File;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadImpl {
    String uploadFile(MultipartFile file);
    boolean deleteFile(String imgUrl);
}
