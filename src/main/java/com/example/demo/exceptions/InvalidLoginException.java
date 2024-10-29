package com.example.demo.exceptions;

public class InvalidLoginException extends Exception{
    public InvalidLoginException() {
    }

    public InvalidLoginException(String message) {
        super(message);
    }
}
