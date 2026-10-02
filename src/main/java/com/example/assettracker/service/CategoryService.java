package com.example.assettracker.service;

import com.example.assettracker.domain.CategoryEntity;
import com.example.assettracker.dtos.CategoryResponse;
import com.example.assettracker.dtos.CreateCategoryRequest;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    Optional<CategoryResponse> getCategoryByCode(String code);

    CategoryResponse createCategory(CreateCategoryRequest request);
}
