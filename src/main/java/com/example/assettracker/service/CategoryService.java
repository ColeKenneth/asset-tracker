package com.example.assettracker.service;

import com.example.assettracker.domain.CategoryEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryEntity> getAllCategories();
    CategoryEntity getCategoryById(Long id);
    Optional<CategoryEntity> getCategoryByCode(String code);
}
