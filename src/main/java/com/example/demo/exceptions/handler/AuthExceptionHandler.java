package com.example.demo.exceptions.handler;

import com.example.demo.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<String> handleInvalidLoginException(InvalidLoginException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format.");
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must contain at least 6 characters, including uppercase, lowercase, digit, and special character.");
    }
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUserException(DuplicateUserException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This email has been taken.");
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @ExceptionHandler(InvalidIINFormat.class)
    public ResponseEntity<String> handleInvalidIINFormat(InvalidIINFormat invalidIINFormat){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IIN code must be exactly 12 digits");
    }



}
