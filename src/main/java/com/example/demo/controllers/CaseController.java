package com.example.demo.controllers;

import com.example.demo.models.Case;
import com.example.demo.services.CaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CaseController {
    private final CaseService caseService;

    @Operation(summary = "All cases")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All cases retrieved")
    })
    @GetMapping("/allCases")
    public ResponseEntity<List<Case>> allCases(){
        List<Case> cases = caseService.allCases();
        return ResponseEntity.status(HttpStatus.OK).body(cases);
    }



}
