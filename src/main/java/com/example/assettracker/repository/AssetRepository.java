package com.example.assettracker.repository;

import com.example.assettracker.domain.AssetEntity;
import com.example.assettracker.domain.AssetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {
    Optional<AssetEntity> findByAssetTag(String assetTag);
    boolean existsByAssetTag(String assetTag);
    List<AssetEntity> findByStatus(AssetStatus status);
    List<AssetEntity> findByNameContainingIgnoreCase(String nameKeyword);

    @Query("""
SELECT a FROM AssetEntity a WHERE(:status IS NULL OR a.status = :status)
AND (:categoryId IS NULL OR a.category.categoryId = :categoryId)
AND (:search IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))
OR LOWER(a.assetTag) LIKE LOWER(CONCAT('%', :search, '%')))
""")
    Page<AssetEntity> findWithFilters(
            @Param("status") AssetStatus status,
            @Param("categoryId") Long categoryId,
            @Param("search") String search,
            Pageable pageable
    );
}
