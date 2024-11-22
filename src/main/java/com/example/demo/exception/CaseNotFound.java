package com.example.demo.exception;

public class CaseNotFound extends Exception{

    public CaseNotFound() {
    }
    public CaseNotFound(String message) {
        super(message);
    }
}
