package com.example.demo.exceptions;

public class AnalystAlreadyExistsException extends Exception{
    public AnalystAlreadyExistsException() {
    }

    public AnalystAlreadyExistsException(String message) {
        super(message);
    }
}
