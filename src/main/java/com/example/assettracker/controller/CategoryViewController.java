package com.example.assettracker.controller;

import com.example.assettracker.dtos.CreateCategoryRequest;
import com.example.assettracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryViewController {
    private final CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("createCategoryRequest", new CreateCategoryRequest("", ""));
        return "categories/list";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createCategory(
           @Valid @ModelAttribute("createCategoryRequest") CreateCategoryRequest request,
           BindingResult bindingResult,
           Model model,
           RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "categories/list";
        }
        categoryService.createCategory(request);
        redirectAttributes.addFlashAttribute("successMessage", "Category created successfully.");
        return "redirect:/categories";
    }
}
