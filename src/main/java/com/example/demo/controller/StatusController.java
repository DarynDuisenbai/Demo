package com.example.demo.controller;

import com.example.demo.service.spec.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Tag(name = "Status Controller",  description = "Endpoints for managing statuses")
@RequiredArgsConstructor
public class StatusController {
    private final StatusService statusService;

    @Operation(summary = "All statuses", description = "Retrieved all statuses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All statuses retrieved")
    })
    @GetMapping("/allStatus")
    public ResponseEntity<List<String>> allStatus(){
        List<String> allStatuses = statusService.allStatuses();
        return ResponseEntity.ok(allStatuses);
    }
}
