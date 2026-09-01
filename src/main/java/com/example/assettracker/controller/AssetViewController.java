package com.example.assettracker.controller;

import com.example.assettracker.dtos.CreateAssetRequest;
import com.example.assettracker.service.AssetService;
import com.example.assettracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetViewController {
    private final AssetService assetService;
    private final CategoryService categoryService;

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
        return "assets/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createAsset(
            @Valid @ModelAttribute("createAssetRequest") CreateAssetRequest request,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "assets/form";
        }

        assetService.createAsset(request);
        return "redirect:/assets";
    }
}
