package com.example.assettracker.service.impl;

import com.example.assettracker.domain.CategoryEntity;
import com.example.assettracker.exception.ResourceNotFoundException;
import com.example.assettracker.repository.CategoryRepository;
import com.example.assettracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    }

    @Override
    public Optional<CategoryEntity> getCategoryByCode(String code) {
        return categoryRepository.findByCode(code);
    }
}
