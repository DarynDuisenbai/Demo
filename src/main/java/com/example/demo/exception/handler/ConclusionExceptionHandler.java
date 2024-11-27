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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(regionNotFoundException.getMessage());
    }

    @ExceptionHandler(CaseNotFound.class)
    public ResponseEntity<String> handleCaseNotFoundException(CaseNotFound caseNotFound){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(caseNotFound.getMessage());
    }

    @ExceptionHandler(NoConclusionException.class)
    public ResponseEntity<String> handleConclusionNotFoundException(NoConclusionException noConclusionException ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(noConclusionException.getMessage());
    }


    @ExceptionHandler(AnalystAlreadyExistsException.class)
    public ResponseEntity<String> handleAnalystAlreadyExistsException(AnalystAlreadyExistsException analystAlreadyExistsException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Analyst already exists.");
    }

}
