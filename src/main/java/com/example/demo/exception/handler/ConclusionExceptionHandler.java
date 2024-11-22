package com.example.demo.exception.handler;

import com.example.demo.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ConclusionExceptionHandler {

    @ExceptionHandler(InvalidUDFormat.class)
    public ResponseEntity<String> handleInvalidUDFormat(InvalidUDFormat invalidUDFormat){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UD code must be exactly 15 digits");
    }
    @ExceptionHandler(RegionNotFoundException.class)
    public ResponseEntity<String> handleRegionNotFoundException(RegionNotFoundException regionNotFoundException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Region not found.");
    }

    @ExceptionHandler(CaseNotFound.class)
    public ResponseEntity<String> handleCaseNotFoundException(CaseNotFound caseNotFound){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Case not found.");
    }

    @ExceptionHandler(NoConclusionException.class)
    public ResponseEntity<String> handleConclusionNotFoundException(NoConclusionException caseNotFound){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conclusion not found.");
    }


    @ExceptionHandler(AnalystAlreadyExistsException.class)
    public ResponseEntity<String> handleAnalystAlreadyExistsException(AnalystAlreadyExistsException analystAlreadyExistsException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Analyst already exists.");
    }

}
