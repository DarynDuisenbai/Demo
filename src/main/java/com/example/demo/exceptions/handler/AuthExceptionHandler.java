package com.example.demo.exceptions.handler;

import com.example.demo.exceptions.InvalidLoginException;
import com.example.demo.exceptions.InvalidPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<String> handleInvalidSignature(InvalidLoginException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format.");
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidSignature(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must contain at least 8 characters, including uppercase, lowercase, digit, and special character.");
    }
}
