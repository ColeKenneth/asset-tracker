package com.example.assettracker.controller;

import com.example.assettracker.dtos.RegisterRequest;
import com.example.assettracker.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {
    private final AuthenticationService authService;

    @GetMapping
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "/register";
    }
}
