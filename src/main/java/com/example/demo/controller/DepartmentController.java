package com.example.demo.controller;

import com.example.demo.service.spec.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
@Tag(name = "Department Controller",  description = "Endpoints for managing departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @Operation(summary = "All departments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All departments retrieved"),
    })
    @GetMapping("/allDepartments")
    public ResponseEntity<Set<String>> allDepartments(){
        Set<String> allDepartments = departmentService.getAllDepartments();
        return ResponseEntity.ok(allDepartments);
    }

    @Operation(summary = "All regions with same department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All regions retrieved"),
    })
    @GetMapping("/regInDep")
    public ResponseEntity<Set<String>> allRegionsWithSameDepartment(@RequestParam String dep){
        Set<String> allDepartments = departmentService.allRegionsInDep(dep);
        return ResponseEntity.ok(allDepartments);
    }
}
