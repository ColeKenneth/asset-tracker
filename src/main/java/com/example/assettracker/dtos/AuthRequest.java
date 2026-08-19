package com.example.assettracker.dtos;

public record AuthRequest(
        String email,
        String password
) {
}
