package com.example.demo.exceptions;

public class EmptyFieldException extends Exception{
    public EmptyFieldException() {
    }
    public EmptyFieldException(String message) {
        super(message);
    }
}
