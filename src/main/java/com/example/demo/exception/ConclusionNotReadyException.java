package com.example.demo.exception;

public class ConclusionNotReadyException extends Exception{
    public ConclusionNotReadyException() {
    }

    public ConclusionNotReadyException(String message) {
        super(message);
    }
}
