package com.example.assettracker.service.impl;

import com.example.assettracker.domain.*;
import com.example.assettracker.dtos.*;
import com.example.assettracker.exception.ResourceNotFoundException;
import com.example.assettracker.repository.AssetAssignmentHistoryRepository;
import com.example.assettracker.repository.AssetRepository;
import com.example.assettracker.repository.CategoryRepository;
import com.example.assettracker.repository.EmployeeRepository;
import com.example.assettracker.service.AssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AssetServiceImpl implements AssetService {
    private final AssetRepository repository;
    private final EmployeeRepository employeeRepository;
    private final CategoryRepository categoryRepository;
    private final AssetAssignmentHistoryRepository historyRepository;

    @Override
    @Transactional
    public AssetResponse createAsset(CreateAssetRequest request) {
        if (repository.existsByAssetTag(request.assetTag())) {
            log.error("Asset tag already exists: {}", request.assetTag());
            throw new IllegalArgumentException("Asset tag already exists: " + request.assetTag());
        }

        CategoryEntity category = null;
        if (request.categoryId() != null) {
            log.warn("Category not found with ID {}", request.categoryId());
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.categoryId()));
        }

        var entity = AssetEntity.builder()
                .assetTag(request.assetTag())
                .name(request.name())
                .serialNumber(request.serialNumber())
                .status(AssetStatus.AVAILABLE)
                .purchaseCost(request.purchaseCost())
                .purchaseDate(request.purchaseDate())
                .build();

        AssetEntity saved = repository.save(entity);
        log.info("Asset successfully created with ID: {} and tag: {}", saved.getId(), saved.getAssetTag());
        return mapToResponse(saved);
    }

    @Override
    public AssetResponse getAssetById(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + id));

        return mapToResponse(entity);
    }

    @Override
    public AssetResponse getAssetByTag(String assetTag) {
        var entity = repository.findByAssetTag(assetTag)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with tag: " + assetTag));

        return mapToResponse(entity);
    }

    @Override
    public List<AssetResponse> getAllAssets() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AssetResponse> getAssetsByStatus(AssetStatus status) {
        return repository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public AssetResponse updateAsset(Long id, UpdateAssetRequest request) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with an ID of: " + id));

        entity.setName(request.name());
        entity.setStatus(request.status());
        entity.setPurchaseCost(request.purchaseCost());

        if (request.categoryId() != null) {
            var category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.categoryId()));
            entity.setCategory(category);
        } else {
            entity.setCategory(null);
        }

        log.info("Updated asset ID: {}", id);
        return mapToResponse(entity);
    }

    @Override
    @Transactional
    public void deleteAsset(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID: " + id));

        if (entity.getStatus() == AssetStatus.ASSIGNED) {
            throw new IllegalStateException("Cannot delete an asset that is assigned.");
        }

        repository.delete(entity);
        log.info("Deleted asset with ID: {}", id);

    }

    @Override
    @Transactional
    public AssetResponse assignAsset(Long assetId, AssignAssetRequest request) {
        var asset = repository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID of: " + assetId));

        if (asset.getStatus() == AssetStatus.ASSIGNED) {
            throw new IllegalStateException("Asset is already assigned to an employee.");
        }

        var employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID of: " + request.employeeId()));

        if (employee.getStatus() == EmployeeStatus.INACTIVE) {
            throw new IllegalStateException("Cannot assign asset to an inactive employee.");
        }

        asset.setAssignedEmployee(employee);
        asset.setStatus(AssetStatus.ASSIGNED);

        var history = new AssetAssignmentHistoryEntity(asset, employee, LocalDateTime.now());
        historyRepository.save(history);

        log.info("Assigned asset ID {} to employee ID {}", assetId, request.employeeId());
        return mapToResponse(asset);
    }

    @Override
    @Transactional
    public AssetResponse returnAsset(Long assetId) {
        var asset = repository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with ID of: " + assetId));

        if (asset.getStatus() != AssetStatus.ASSIGNED) {
            throw new IllegalStateException("Asset is not currently assigned.");
        }

        historyRepository.findByAssetIdAndReturnedAtIsNull(assetId).ifPresent(history -> {
            history.setReturnedAt(LocalDateTime.now());
            historyRepository.save(history);
        });

        asset.setAssignedEmployee(null);
        asset.setStatus(AssetStatus.AVAILABLE);

        log.info("Returned asset ID {}", assetId);
        return mapToResponse(asset);
    }

    @Override
    public List<AssetAssignmentHistoryResponse> getAssetHistory(Long assetId) {
        if (!repository.existsById(assetId)) {
            log.warn("Asset not found with ID {}", assetId);
            throw new ResourceNotFoundException("Asset not found with ID: " + assetId);
        }

        return historyRepository.findAll().stream()
                .map(this::mapToHistoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetResponse> getAllAssets(AssetStatus status, Long categoryId, String search, Pageable pageable) {
        return repository.findWithFilters(status, categoryId, search, pageable)
                .map(this::mapToResponse);
    }

    private AssetResponse mapToResponse(AssetEntity entity) {
        String categoryName = entity.getCategory() != null ? entity.getCategory().getName() : null;
        String employeeName = entity.getAssignedEmployee() != null ?
                entity.getAssignedEmployee().getFirstName() + " " + entity.getAssignedEmployee().getLastName()
                : null;
        return new AssetResponse(
                entity.getId(),
                entity.getAssetTag(),
                entity.getName(),
                entity.getSerialNumber(),
                entity.getStatus(),
                entity.getPurchaseCost(),
                entity.getPurchaseDate(),
                categoryName,
                employeeName
        );
    }

    private AssetAssignmentHistoryResponse mapToHistoryResponse(AssetAssignmentHistoryEntity history) {
        String employeeName = history.getEmployee() != null ?
                history.getEmployee().getFirstName() + " " + history.getEmployee().getLastName()
                : null;
        return new AssetAssignmentHistoryResponse(
                history.getId(),
                history.getAsset().getId(),
                history.getAsset().getAssetTag(),
                history.getEmployee() != null ? history.getEmployee().getId() : null,
                employeeName,
                history.getAssignedAt(),
                history.getReturnedAt()
        );
    }
}
