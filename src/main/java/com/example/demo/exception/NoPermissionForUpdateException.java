package com.example.demo.exception;

public class NoPermissionForUpdateException extends Exception {
    public NoPermissionForUpdateException() {
    }
    public NoPermissionForUpdateException(String message) {
        super(message);
    }
}
