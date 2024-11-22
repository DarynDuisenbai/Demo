package com.example.demo.exception;

public class RegionNotFoundException extends Exception{
    public RegionNotFoundException() {
    }

    public RegionNotFoundException(String message) {
        super(message);
    }
}
