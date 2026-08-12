package com.example.assettracker.service;

import com.example.assettracker.domain.AssetAssignmentHistoryEntity;
import com.example.assettracker.domain.AssetStatus;
import com.example.assettracker.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetService {
    AssetResponse createAsset(CreateAssetRequest request);
    AssetResponse getAssetById(Long id);
    AssetResponse getAssetByTag(String assetTag);
    List<AssetResponse> getAllAssets();
    List<AssetResponse> getAssetsByStatus(AssetStatus status);
    AssetResponse updateAsset(Long id, UpdateAssetRequest request);
    void deleteAsset(Long id);
    AssetResponse assignAsset(Long assetId, AssignAssetRequest request);
    AssetResponse returnAsset(Long assetId);
    List<AssetAssignmentHistoryResponse> getAssetHistory(Long id);
    Page<AssetResponse> getAllAssets(AssetStatus status, Long categoryId, String search, Pageable pageable);
}
