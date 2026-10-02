package com.example.assettracker.service.impl;

import com.example.assettracker.domain.CategoryEntity;
import com.example.assettracker.dtos.CategoryResponse;
import com.example.assettracker.dtos.CreateCategoryRequest;
import com.example.assettracker.exception.ResourceNotFoundException;
import com.example.assettracker.repository.CategoryRepository;
import com.example.assettracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
    }

    @Override
    public Optional<CategoryResponse> getCategoryByCode(String code) {
        return categoryRepository.findByCode(code)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        var entity = new CategoryEntity();
        entity.setName(request.name());
        entity.setDescription(request.description());

        var saved = categoryRepository.save(entity);
        log.info("Successfully created category with ID: {}", saved.getCategoryId());
        return mapToResponse(saved);
    }

    private CategoryResponse mapToResponse(CategoryEntity entity) {
        return new CategoryResponse(
                entity.getCategoryId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
