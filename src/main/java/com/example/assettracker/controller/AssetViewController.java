package com.example.assettracker.controller;

import com.example.assettracker.dtos.AssignAssetRequest;
import com.example.assettracker.dtos.CreateAssetRequest;
import com.example.assettracker.dtos.UpdateAssetRequest;
import com.example.assettracker.service.AssetService;
import com.example.assettracker.service.CategoryService;
import com.example.assettracker.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetViewController {
    private final AssetService assetService;
    private final CategoryService categoryService;
    private final EmployeeService employeeService;

    @GetMapping
    public String listAssets(Model model) {
        model.addAttribute("assets", assetService.getAllAssets());
        return "assets/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("assetRequest", new CreateAssetRequest(
                "", "", "", BigDecimal.ZERO, LocalDate.now(), null)
        );
        model.addAttribute("categories", categoryService.getAllCategories());
        return "assets/create";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createAsset(
            @Valid @ModelAttribute("assetRequest") CreateAssetRequest request,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "assets/create";
        }

        assetService.createAsset(request);
        return "redirect:/assets";
    }

    @GetMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAssignForm(@PathVariable Long id, Model model) {
        model.addAttribute("asset", assetService.getAssetById(id));
        model.addAttribute("assignAssetRequest", new AssignAssetRequest(null));
        model.addAttribute("employees", employeeService.getActiveEmployees());
        return "assets/assign-form";
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignAsset(
            @PathVariable Long id,
            @Valid @ModelAttribute("assignAssetRequest") AssignAssetRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("asset", assetService.getAssetById(id));
            model.addAttribute("employees", employeeService.getActiveEmployees());
            return "assets/assign-form";
        }

        assetService.assignAsset(id, request);
        return "redirect:/assets";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        var asset = assetService.getAssetById(id);

        var request = new UpdateAssetRequest(
                asset.id(),
                asset.name(),
                asset.status(),
                asset.purchaseCost(),
                null
        );

        model.addAttribute("asset", request);
        model.addAttribute("id", id);
        return "assets/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateAsset(
            @PathVariable Long id,
            @Valid @ModelAttribute("asset") UpdateAssetRequest request,
            BindingResult bindingResult,
            Model model
            ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            return "assets/edit";
        }

        assetService.updateAsset(id, request);
        return "redirect:/assets";
    }
}
