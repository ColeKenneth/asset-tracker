package com.example.assettracker.controller;

import com.example.assettracker.dtos.CreateEmployeeRequest;
import com.example.assettracker.dtos.UpdateEmployeeRequest;
import com.example.assettracker.exception.ResourceNotFoundException;
import com.example.assettracker.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("employee", new CreateEmployeeRequest("", "", "", "", ""));
        model.addAttribute("isEdit", false);
        return "employees/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createEmployee(
            @Valid @ModelAttribute("employee") CreateEmployeeRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "employees/form";
        }

        employeeService.createEmployee(request);
        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        var employee = employeeService.getEmployeeById(id);
        var updateRequest = new UpdateEmployeeRequest(
                employee.employeeId(),
                employee.firstName(),
                employee.lastName(),
                employee.email(),
                employee.department(),
                employee.status()
        );

        model.addAttribute("employee", updateRequest);
        model.addAttribute("employeeId", id);
        model.addAttribute("isEdit", true);
        return "employees/form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") UpdateEmployeeRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeId", id);
            model.addAttribute("isEdit", true);
            return "employees/form";
        }

        employeeService.updateEmployee(id, request);
        return "redirect:/employees";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "Employee was deleted successfully.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found.");
        }

        return "redirect:/employees";
    }
}