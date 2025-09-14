package com.example.billing.Service.Category;

import com.example.billing.io.CategoryRequest;
import com.example.billing.io.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryImpl {
    CategoryResponse add(CategoryRequest request, MultipartFile file);
    List<CategoryResponse> get();
    void deleteCategory(String categoryId);
}
