package com.example.assettracker.dtos;

import com.example.assettracker.domain.Role;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        Role role
) {
    public RegisterRequest() {
        this("", "", "", "", Role.USER);
    }
}
