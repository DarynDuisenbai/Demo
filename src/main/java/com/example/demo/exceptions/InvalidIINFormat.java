package com.example.demo.exceptions;

public class InvalidIINFormat extends Exception{
    public InvalidIINFormat() {
    }

    public InvalidIINFormat(String message) {
        super(message);
    }
}
