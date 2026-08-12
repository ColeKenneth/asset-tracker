package com.example.assettracker.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        Object message,
        LocalDateTime timestamp
) {
}
