package com.example.demo.controller;

import com.example.demo.domain.Business;
import com.example.demo.service.spec.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Businesses", description = "API for managing business relations")
public class BusinessController {
    private final BusinessService businessService;

    @Operation(summary = "All business types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All businesses returned successfully"),
    })
    @GetMapping("/allBusinesses")
    public ResponseEntity<List<String>> allBusinesses(){
        List<String> allActions = businessService.allBusinesses();
        return ResponseEntity.ok(allActions);
    }
}
