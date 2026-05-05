package com.smartfinance.userservice.dto;

public record MeResponse(String username, String role, boolean authenticated) {
}
