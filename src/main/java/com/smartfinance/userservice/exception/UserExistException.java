package com.smartfinance.userservice.exception;

public class UserExistException extends BusinessException{
    public UserExistException(String username){
        super("User "+username+" already exist.");
    }
}
