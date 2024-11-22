package com.example.demo.exception;

public class InvalidPasswordException extends Exception{
    public InvalidPasswordException() {
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
