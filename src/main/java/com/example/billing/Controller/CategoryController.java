package com.example.billing.Controller;

import com.example.billing.Service.Category.CategoryService;
import com.example.billing.io.CategoryRequest;
import com.example.billing.io.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController

public class CategoryController {
    @Autowired
    private CategoryService categoryService;


        @PostMapping("/admin/categories")
        @ResponseStatus(HttpStatus.CREATED)
        public CategoryResponse addCategory(@RequestParam String name,  @RequestParam String description,  @RequestParam String bgColor,@RequestPart(value = "file", required = false) MultipartFile file) {
            try {
                CategoryRequest request = new CategoryRequest();
                request.setName(name);
                request.setDescription(description);
                request.setBgColor(bgColor);
                return categoryService.add(request, file);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exception occurred while creating category: " + e.getMessage());         }
        }

    @GetMapping("/category")
    public List<CategoryResponse> getAllCategory(){
        return categoryService.get();
    }

    @DeleteMapping("/admin/category/{categoryId}")
    public ResponseEntity<String> delete(@PathVariable String categoryId){
        try{
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}