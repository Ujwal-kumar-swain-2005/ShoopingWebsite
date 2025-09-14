package com.example.billing.Service.Category;

import com.example.billing.Entity.CategoryEntity;
import com.example.billing.Repository.CategoryRepository;
import com.example.billing.Repository.ItemRepository;
import com.example.billing.Service.File.FileUploadService;
import com.example.billing.io.CategoryRequest;
import com.example.billing.io.CategoryResponse;
import com.example.billing.io.ItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService implements CategoryImpl {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public CategoryResponse add(CategoryRequest request, MultipartFile file) {
        try {
            CategoryEntity categoryEntity = convertToEntity(request);

            if (file != null && !file.isEmpty()) {
                String fileUrl = fileUploadService.uploadFile(file);
                categoryEntity.setImgUrl(fileUrl);
            }

            CategoryEntity newCategoryEntity = categoryRepository.save(categoryEntity);
            return convertToResponse(newCategoryEntity);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create category: " + e.getMessage());
        }
    }

    @Override
    public List<CategoryResponse> get() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(String categoryId) {
        CategoryEntity existingCategory = categoryRepository
                .findByCategoryId(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Delete the associated file first
        if (existingCategory.getImgUrl() != null && !existingCategory.getImgUrl().isEmpty()) {
            try {
                fileUploadService.deleteFile(existingCategory.getImgUrl());
            } catch (Exception e) {
                // Log the error but don't fail the category deletion
                System.err.println("Failed to delete associated file: " + e.getMessage());
            }
        }

        // Delete the category
        categoryRepository.delete(existingCategory);
    }

    private CategoryResponse convertToResponse(CategoryEntity categoryEntity) {
        Integer itemCount = itemRepository.countByCategoryId(categoryEntity.getId());
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryId(categoryEntity.getCategoryId());
        categoryResponse.setBgColor(categoryEntity.getBgColor());
        categoryResponse.setDescription(categoryEntity.getDescription());
        categoryResponse.setName(categoryEntity.getName());
        categoryResponse.setImgUrl(categoryEntity.getImgUrl());
        categoryResponse.setCreatedAt(categoryEntity.getCreatedAt());
        categoryResponse.setUpdatedAt(categoryEntity.getUpdateAt());
        categoryResponse.setItems(itemCount);
        return categoryResponse;
    }

    private CategoryEntity convertToEntity(CategoryRequest request) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(UUID.randomUUID().toString());
        categoryEntity.setName(request.getName());
        categoryEntity.setBgColor(request.getBgColor());
        categoryEntity.setDescription(request.getDescription());
        return categoryEntity;
    }
}
