package com.example.assettracker.controller;

import com.example.assettracker.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetViewController {
    private final AssetService assetService;

    @GetMapping
    public String listAssets(Model model) {
        model.addAttribute("assets", assetService.getAllAssets());
        return "assets/list";
    }
}
