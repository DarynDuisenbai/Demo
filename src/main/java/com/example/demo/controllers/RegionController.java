package com.example.demo.controllers;

import com.example.demo.services.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@Tag(name = "Region Controller",  description = "Endpoints for managing regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;
    @Operation(summary = "All regions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All regions retrieved"),
    })
    @GetMapping("/allRegions")
    public ResponseEntity<List<String>> allRegions(){
        List<String> allRegions = regionService.allRegions();
        return ResponseEntity.ok(allRegions);
    }

    @Operation(summary = "All regions with same department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All regions retrieved"),
    })
    @GetMapping("/regInDep")
    public ResponseEntity<Set<String>> allRegionsWithSameDepartment(@RequestParam String dep){
        Set<String> allDepartments = regionService.allRegionsInDep(dep);
        return ResponseEntity.ok(allDepartments);
    }
}
