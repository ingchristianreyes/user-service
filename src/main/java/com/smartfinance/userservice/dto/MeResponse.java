package com.smartfinance.userservice.dto;

import java.util.Set;

public class MeResponse {

    private String username;
    private Set<String> authorities;
    private boolean authenticated;

    public MeResponse(String username, Set<String> authorities, boolean authenticated) {
        this.username = username;
        this.authorities = authorities;
        this.authenticated = authenticated;
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}