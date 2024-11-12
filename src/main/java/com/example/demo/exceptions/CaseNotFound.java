package com.example.demo.exceptions;

public class CaseNotFound extends Exception{

    public CaseNotFound() {
    }
    public CaseNotFound(String message) {
        super(message);
    }
}
