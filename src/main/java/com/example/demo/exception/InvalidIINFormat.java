package com.example.demo.exception;

public class InvalidIINFormat extends Exception{
    public InvalidIINFormat() {
    }

    public InvalidIINFormat(String message) {
        super(message);
    }
}
