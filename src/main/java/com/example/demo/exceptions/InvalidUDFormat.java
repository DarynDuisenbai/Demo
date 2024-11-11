package com.example.demo.exceptions;

public class InvalidUDFormat extends Exception{
    public InvalidUDFormat() {
    }
    public InvalidUDFormat(String message) {
        super(message);
    }
}
