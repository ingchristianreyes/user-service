package com.smartfinance.userservice.dto;

public record LoginResponse(String token, String tokenType) {

    public LoginResponse(String token) {
        this(token,"Bearer");
    }

    public LoginResponse{
        if(tokenType==null){
            tokenType="Bearer";
        }
    }
}
