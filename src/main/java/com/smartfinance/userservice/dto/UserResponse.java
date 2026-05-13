package com.smartfinance.userservice.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        Set<String> roles,
        Boolean enabled,
        LocalDateTime createdAt
) {
}
