package com.example.demo.exception;

public class DuplicateUserException extends Exception{
    public DuplicateUserException() {
    }
    public DuplicateUserException(String message) {
        super(message);
    }
}
