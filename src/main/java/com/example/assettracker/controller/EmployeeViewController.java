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

        model.addAttribute("employeeId", id);
        model.addAttribute("updateEmployeeRequest", updateRequest);
        return "employees/form";
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
