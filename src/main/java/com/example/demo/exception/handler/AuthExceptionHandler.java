package com.example.demo.exception.handler;

import com.example.demo.exception.*;
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUserException(DuplicateUserException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This email has been taken.");
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidIINFormat.class)
    public ResponseEntity<String> handleInvalidIINFormat(InvalidIINFormat invalidIINFormat){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IIN code must be exactly 12 digits");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(accessDeniedException.getMessage());
    }



}
