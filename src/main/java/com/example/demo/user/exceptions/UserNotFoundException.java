package com.example.demo.user.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
