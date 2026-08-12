package com.example.assettracker.controller;

import com.example.assettracker.domain.AssetStatus;
import com.example.assettracker.dtos.*;
import com.example.assettracker.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<AssetResponse> createAsset(@Valid @RequestBody CreateAssetRequest request) {
        var createdAsset = assetService.createAsset(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdAsset.id())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponse> getAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @GetMapping("/tag/{assetTag}")
    public ResponseEntity<AssetResponse> getAssetByTag(@PathVariable String assetTag) {
        return ResponseEntity.ok(assetService.getAssetByTag(assetTag));
    }

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAllAssets(@RequestParam(required = false)AssetStatus status) {
        if (status != null) {
            return ResponseEntity.ok(assetService.getAssetsByStatus(status));
        }

        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponse> updateAsset(@PathVariable Long id, @Valid @RequestBody UpdateAssetRequest request) {
        return ResponseEntity.ok(assetService.updateAsset(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<AssetResponse> assignAsset(@PathVariable Long id, @Valid @RequestBody AssignAssetRequest request) {
        return ResponseEntity.ok(assetService.assignAsset(id, request));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<AssetResponse> returnAsset(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.returnAsset(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<AssetAssignmentHistoryResponse>> getAssetHistory(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getAssetHistory(id));
    }

    public ResponseEntity<Page<AssetResponse>> getAllAssets(
            @RequestParam(required = false) AssetStatus status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<AssetResponse> assets = assetService.getAllAssets(status, categoryId, search, pageable);
        return ResponseEntity.ok(assets);
    }
}
