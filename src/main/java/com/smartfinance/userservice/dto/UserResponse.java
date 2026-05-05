package com.smartfinance.userservice.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String role,
        Boolean enabled,
        LocalDateTime createdAt
) {
}
