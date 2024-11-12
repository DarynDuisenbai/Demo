package com.example.demo.controllers;

import com.example.demo.dtos.requests.*;
import com.example.demo.dtos.responces.AgreementDto;
import com.example.demo.dtos.responces.ConclusionDto;
import com.example.demo.dtos.responces.TempConclusionDto;
import com.example.demo.exceptions.CaseNotFound;
import com.example.demo.exceptions.NoTemporaryConclusionFound;
import com.example.demo.exceptions.RegionNotFoundException;
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
import java.time.LocalDateTime;
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
    public ResponseEntity<?> createConclusion(@Valid @RequestBody CreateConclusionRequest createConclusionRequest) throws UserNotFoundException, RegionNotFoundException, CaseNotFound {
        conclusionService.createConclusion(createConclusionRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Document successfully created.");
    }

    @Operation(summary = "Turn temporary to permanent", description = "Turns temporary conclusion into permanent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document successfully created"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @PostMapping("/turn")
    public ResponseEntity<?> turnConclusion(@RequestParam String registrationNumber) throws UserNotFoundException, NoTemporaryConclusionFound {
        conclusionService.turnToPermanent(registrationNumber);
        return ResponseEntity.status(HttpStatus.OK).body("Document successfully created.");
    }



    @Operation(summary = "Filter conclusions", description = "Filter conclusions based on specified criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered conclusions returned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @GetMapping("/filter")
    public ResponseEntity<List<ConclusionDto>> filter(@RequestParam(required = false) String registrationNumber,
                                                      @RequestParam(required = false) String status,
                                                      @RequestParam(required = false) String region,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime from,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime to,
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
    @GetMapping("/usersDocs")
    public ResponseEntity<List<ConclusionDto>> myConclusions(@RequestParam String IIN) throws UserNotFoundException {
        List<ConclusionDto> conclusions = conclusionService.userConclusions(IIN);
        return ResponseEntity.ok(conclusions);
    }

    @Operation(summary = "Get user's temporary conclusions", description = "Retrieves a list of temporary conclusions associated with a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conclusions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @GetMapping("/temps")
    public ResponseEntity<List<TempConclusionDto>> myTempConclusions(@RequestParam String IIN) throws UserNotFoundException {
        List<TempConclusionDto> conclusions = conclusionService.userSavedConclusions(IIN);
        return ResponseEntity.ok(conclusions);
    }


    @Operation(summary = "Get all UD", description = "Retrieves a list of UD's")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All UD's retrieved")
    })
    @GetMapping("/allUD")
    public ResponseEntity<List<String>> allUD(){
        List<String> allUD = conclusionService.allUD();
        return ResponseEntity.ok(allUD);
    }

    @Operation(summary = "Save a temporary conclusion", description = "Saving option for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved a temporary conclusion")
    })
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CreateConclusionRequest createConclusionRequest)
            throws UserNotFoundException, RegionNotFoundException {
        conclusionService.saveConclusion(createConclusionRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Document successfully saved.");
    }

    @Operation(summary = "Edit a temporary conclusion", description = "After saving user can continue to edit it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited a temporary conclusion")
    })
    @PutMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody EditSavedConclusionRequest editSavedConclusionRequest)
            throws UserNotFoundException, RegionNotFoundException, NoTemporaryConclusionFound {
        conclusionService.editSavedConclusion(editSavedConclusionRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Document successfully edited.");
    }


//    @Operation(summary = "Making decision for conclusion", description = " «На согласовании», «Согласовано», «Отказано», «Оставлено без рассмотрения», «Отправлено на доработку».")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Decision was made")
//    })
//    @PostMapping("/decision")
//    public ResponseEntity<?> decision(DecisionRequest decisionRequest){
//        conclusionService.
//    }


}
