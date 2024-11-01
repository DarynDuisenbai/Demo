package com.example.demo.exceptions;

public class DuplicateUserException extends Exception{
    public DuplicateUserException() {
    }
    public DuplicateUserException(String message) {
        super(message);
    }
}
