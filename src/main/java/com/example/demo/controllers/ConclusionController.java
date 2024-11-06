package com.example.demo.controllers;

import com.example.demo.dtos.requests.CreateConclusionRequest;
import com.example.demo.dtos.requests.FilterRequest;
import com.example.demo.dtos.requests.ShortConclusionRequest;
import com.example.demo.dtos.requests.UserConclusionRequest;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.Region;
import com.example.demo.services.ConclusionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "Conclusions", description = "API for managing conclusions")
public class ConclusionController {

    private final ConclusionService conclusionService;

    @Operation(summary = "Create a new conclusion", description = "Creates a new conclusion and associates it with a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document successfully created"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @PostMapping("/create")
    //@PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createConclusion(@Valid @RequestBody CreateConclusionRequest createConclusionRequest) throws UserNotFoundException {
        conclusionService.createConclusion(createConclusionRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Document successfully created.");
    }

    @Operation(summary = "Filter conclusions", description = "Filter conclusions based on specified criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered conclusions returned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @GetMapping("/filter")
   // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ConclusionDto>> filter(@RequestParam(required = false) String registrationNumber,
                                                      @RequestParam(required = false) Integer status,
                                                      @RequestParam(required = false) Region region,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                                      @RequestParam(required = false) String iin,
                                                      @RequestParam(required = false) String ud,
                                                      @RequestParam(required = false) String fullName) {
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setRegistrationNumber(registrationNumber);
        filterRequest.setStatus(status);
        filterRequest.setRegion(region);
        filterRequest.setFrom(from);
        filterRequest.setTo(to);
        filterRequest.setIIN(iin);
        filterRequest.setUD(ud);
        filterRequest.setFullName(fullName);
        List<ConclusionDto> results = conclusionService.filter(filterRequest);
        return ResponseEntity.ok(results);
    }

    /*@Operation(summary = "Get agreement of conclusions", description = "Returns a list of conclusions based on agreement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agreement of conclusions returned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @GetMapping("/short")
    public ResponseEntity<List<AgreementDto>> agreement(@RequestBody AgreementDto agreementDto) throws UserNotFoundException {
        List<AgreementDto> results = conclusionService.agreement(agreementDto);
        return ResponseEntity.ok(results);
    }*/


    @Operation(summary = "Get user's conclusions", description = "Retrieves a list of conclusions associated with a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conclusions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @GetMapping("/long")
    //@PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ConclusionDto>> myConclusions(@RequestBody UserConclusionRequest userConclusionRequest) throws UserNotFoundException {
        List<ConclusionDto> conclusions = conclusionService.userConclusions(userConclusionRequest);
        return ResponseEntity.ok(conclusions);
    }

}
