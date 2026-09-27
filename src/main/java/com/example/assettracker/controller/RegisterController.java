package com.example.assettracker.controller;

import com.example.assettracker.dtos.RegisterRequest;
import com.example.assettracker.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RegisterController {
    private final AuthenticationService authService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                               BindingResult result, Model model) {
        log.info("Processing registration for user: {}", request.email());

        if (result.hasErrors()) return "register";

        try {
            authService.register(request);
            return "redirect:/login?registered";
        } catch (Exception e) {
            log.error("Registration failed", e);
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
