package com.example.demo.exceptions;

public class RegionNotFoundException extends Exception{
    public RegionNotFoundException() {
    }

    public RegionNotFoundException(String message) {
        super(message);
    }
}
