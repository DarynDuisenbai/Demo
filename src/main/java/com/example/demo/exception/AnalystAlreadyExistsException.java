package com.example.demo.exception;

public class AnalystAlreadyExistsException extends Exception{
    public AnalystAlreadyExistsException() {
    }

    public AnalystAlreadyExistsException(String message) {
        super(message);
    }
}
