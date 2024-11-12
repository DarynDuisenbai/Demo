package com.example.demo.exceptions.handler;

import com.example.demo.exceptions.CaseNotFound;
import com.example.demo.exceptions.InvalidUDFormat;
import com.example.demo.exceptions.RegionNotFoundException;
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


}
