package com.smartfinance.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Size(min = 4, max = 100)
        String username,
        @NotBlank
        @Size(min = 6, max = 100)
        String password
) {
}
