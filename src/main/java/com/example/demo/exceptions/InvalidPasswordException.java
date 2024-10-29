package com.example.demo.exceptions;

public class InvalidPasswordException extends Exception{
    public InvalidPasswordException() {
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
