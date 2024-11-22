package com.example.demo.exception;

public class EmptyFieldException extends Exception{
    public EmptyFieldException() {
    }
    public EmptyFieldException(String message) {
        super(message);
    }
}
