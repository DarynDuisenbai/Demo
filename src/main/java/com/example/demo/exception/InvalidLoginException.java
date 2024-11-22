package com.example.demo.exception;

public class InvalidLoginException extends Exception{
    public InvalidLoginException() {
    }

    public InvalidLoginException(String message) {
        super(message);
    }
}
