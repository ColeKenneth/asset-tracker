package com.example.assettracker.repository;

import com.example.assettracker.domain.AssetEntity;
import com.example.assettracker.domain.AssetStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AssetRepositoryTest {
    @Autowired
    private AssetRepository assetRepository;

    @Test
    @DisplayName("Should persist asset and retrieve it by asset tag")
    public void shouldSaveAndFindByAssetTag() {
        AssetEntity newAsset = AssetEntity.builder()
                .assetTag("SRV-2026-001")
                .name("Dell PowerEdge R750")
                .serialNumber("SN-7781029")
                .status(AssetStatus.AVAILABLE)
                .purchaseCost(new BigDecimal("45000.00"))
                .purchaseDate(LocalDate.of(2026, 3, 15))
                .build();

        assetRepository.save(newAsset);
        Optional<AssetEntity> retrieved = assetRepository.findByAssetTag("SRV-2026-001");

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Dell PowerEdge R750");
        assertThat(retrieved.get().getStatus()).isEqualTo(AssetStatus.AVAILABLE);

    }
}

