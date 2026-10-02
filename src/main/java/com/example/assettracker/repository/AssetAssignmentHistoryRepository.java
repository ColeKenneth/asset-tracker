package com.example.assettracker.repository;

import com.example.assettracker.domain.AssetAssignmentHistoryEntity;
import com.example.assettracker.dtos.AssetAssignmentHistoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetAssignmentHistoryRepository extends JpaRepository<AssetAssignmentHistoryEntity, Long> {
    Optional<AssetAssignmentHistoryEntity> findByAssetIdAndReturnedAtIsNull(Long assetId);
    void deleteByAssetId(Long assetId);
    List<AssetAssignmentHistoryEntity> findByAssetIdOrderByAssignedAtDesc(Long assetId);
}
