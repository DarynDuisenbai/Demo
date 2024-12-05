package com.example.demo.controller;

import com.example.demo.service.spec.EventPlacesService;
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
@Tag(name = "Event places Controller", description = "Endpoint for managing event places")
public class EventPlacesController {

    private final EventPlacesService eventPlacesService;

    @Operation(summary = "All event places")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All event places retrieved"),
    })
    @GetMapping("/allEventPlaces")
    public ResponseEntity<List<String>> allEventPlaces(){
        List<String> allEventPlaces = eventPlacesService.allEventPlaces();
        return ResponseEntity.ok(allEventPlaces);
    }
}
