package com.example.assettracker.service;

import com.example.assettracker.domain.*;
import com.example.assettracker.dtos.AssignAssetRequest;
import com.example.assettracker.repository.AssetAssignmentHistoryRepository;
import com.example.assettracker.repository.AssetRepository;
import com.example.assettracker.repository.CategoryRepository;
import com.example.assettracker.repository.EmployeeRepository;
import com.example.assettracker.service.impl.AssetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssetServiceImplTest {
    @Mock
    private AssetRepository assetRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AssetAssignmentHistoryRepository historyRepository;

    @InjectMocks
    private AssetServiceImpl assetService;

    private AssetEntity availableAsset;
    private EmployeeEntity activeEmployee;

    @BeforeEach
    public void setUp() {
        availableAsset = AssetEntity.builder()
                .id(1L)
                .assetTag("AST-001")
                .name("MacBook Pro 16")
                .serialNumber("SN-9999")
                .status(AssetStatus.AVAILABLE)
                .purchaseCost(new BigDecimal("2499.99"))
                .purchaseDate(LocalDate.now())
                .build();

        activeEmployee = EmployeeEntity.builder()
                .id(10L)
                .employeeId("EMP-101")
                .firstName("Brian")
                .lastName("Johnson")
                .email("brianjohnson@gmail.com")
                .status(EmployeeStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("assignAsset - success")
    public void assignAssetSuccess() {
        when(assetRepository.findById(1L)).thenReturn(Optional.of(availableAsset));
        when(employeeRepository.findById(10L)).thenReturn(Optional.of(activeEmployee));

        var response = assetService.assignAsset(1L, new AssignAssetRequest(10L));

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(AssetStatus.ASSIGNED);
        assertThat(response.assignedEmployeeName()).isEqualTo("Brian Johnson");

        verify(historyRepository, times(1)).save(any(AssetAssignmentHistoryEntity.class));
    }

    @Test
    @DisplayName("assignAsset - Fails when asset is already assigned")
    public void assignAssetAlreadyAssignedThrowsException() {
        availableAsset.setStatus(AssetStatus.ASSIGNED);
        when(assetRepository.findById(1L)).thenReturn(Optional.of(availableAsset));

        assertThatThrownBy(() -> assetService.assignAsset(1L, new AssignAssetRequest(10L)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Asset is already assigned to an employee.");

        verify(historyRepository, never()).save(any());
    }

    @Test
    @DisplayName("assignAsset - Fails when asset is assigned to an inactive employee")
    public void assignAssetInactiveEmployeeThrowsException() {
        activeEmployee.setStatus(EmployeeStatus.INACTIVE);

        when(assetRepository.findById(1L)).thenReturn(Optional.of(availableAsset));
        when(employeeRepository.findById(10L)).thenReturn(Optional.of(activeEmployee));

        assertThatThrownBy(() -> assetService.assignAsset(1L, new AssignAssetRequest(10L)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot assign asset to an inactive employee");

        verify(historyRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteAsset - Fails when asset is currently assigned")
    public void deleteAssetAssignedAssetThrowsException() {
        availableAsset.setStatus(AssetStatus.ASSIGNED);
        when(assetRepository.findById(1L)).thenReturn(Optional.of(availableAsset));

        assertThatThrownBy(() -> assetService.deleteAsset(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot delete an asset that is assigned.");

        verify(historyRepository, never()).delete(any());
    }

    @Test
    @DisplayName("deleteAsset - succeeds when asset is available")
    public void deleteAssetAvailableAssetSuccess() {
        when(assetRepository.findById(1L)).thenReturn(Optional.of(availableAsset));

        assetService.deleteAsset(1L);
        verify(assetRepository, times(1)).delete(availableAsset);
    }
}
