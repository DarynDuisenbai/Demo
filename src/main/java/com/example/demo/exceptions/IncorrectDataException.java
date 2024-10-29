package com.example.demo.exceptions;

public class IncorrectDataException extends Exception{
    public IncorrectDataException() {
    }
    public IncorrectDataException(String message) {
        super(message);
    }
}
