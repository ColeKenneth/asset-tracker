package com.example.assettracker.controller;

import com.example.assettracker.dtos.CreateEmployeeRequest;
import com.example.assettracker.service.EmployeeService;
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

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeViewController {
    private final EmployeeService employeeService;

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("createEmployeeRequest", new CreateEmployeeRequest("", "", "", "", ""));
        return "employees/form";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createEmployee(
            @Valid @ModelAttribute("createEmployeeRequest") CreateEmployeeRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "employees/form";
        }

        employeeService.createEmployee(request);
        return "redirect:/employees";
    }


}
