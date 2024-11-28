package com.example.demo.controller;

import com.example.demo.dto.request.conclusion.CreateConclusionRequest;
import com.example.demo.dto.request.conclusion.EditSavedConclusionRequest;
import com.example.demo.dto.request.conclusion.FilterRequest;
import com.example.demo.dto.request.user.DecisionRequest;
import com.example.demo.dto.request.user.EditProfileRequest;
import com.example.demo.dto.responce.AgreementDto;
import com.example.demo.dto.responce.ConclusionDto;
import com.example.demo.dto.responce.History;
import com.example.demo.dto.responce.TempConclusionDto;
import com.example.demo.exception.*;
import com.example.demo.service.spec.ConclusionService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    public ResponseEntity<?> turnConclusion(@RequestParam String registrationNumber) throws UserNotFoundException, NoTemporaryConclusionFound, ConclusionNotReadyException {
        conclusionService.turnToPermanent(registrationNumber);
        return ResponseEntity.status(HttpStatus.OK).body("Document successfully created.");
    }



    @Operation(summary = "Filter conclusions", description = "Filter conclusions based on specified criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered conclusions returned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @GetMapping("/filter")
    public ResponseEntity<Set<ConclusionDto>> filter(@RequestParam(required = false) String registrationNumber,
                                                      @RequestParam(required = false) String status,
                                                      @RequestParam(required = false) String region,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime from,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime to,
                                                      @RequestParam String iin,
                                                      @RequestParam(required = false) String ud,
                                                      @RequestParam(required = false) String fullName,
                                                     @RequestParam(required = false) String iinOfCalled) throws UserNotFoundException {
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setRegistrationNumber(registrationNumber);
        filterRequest.setStatus(status);
        filterRequest.setRegion(region);
        filterRequest.setFrom(from);
        filterRequest.setTo(to);
        filterRequest.setIIN(iin);
        filterRequest.setUD(ud);
        filterRequest.setFullName(fullName);
        filterRequest.setIinOfCalled(iinOfCalled);
        Set<ConclusionDto> results = conclusionService.filter(filterRequest);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Get user's conclusions", description = "Retrieves a list of conclusions associated with a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conclusions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @GetMapping("/usersDocs")
    public ResponseEntity<Set<ConclusionDto>> myConclusions(@RequestParam String IIN) throws UserNotFoundException {
        Set<ConclusionDto> conclusions = conclusionService.userConclusions(IIN);
        return ResponseEntity.ok(conclusions);
    }

    @Operation(summary = "All user's agreements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All user's agreements retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/usersAgreements")
    public ResponseEntity<?> myAgreements(@RequestParam String IIN) throws UserNotFoundException {
        List<AgreementDto> agreements = conclusionService.userAgreements(IIN);
        return ResponseEntity.ok(agreements);
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

    @Operation(summary = "Get specific document", description = "All information about specific document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conclusions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Conclusion not found.")
    })
    @GetMapping("/fullConclusion")
    public ResponseEntity<?> getSpecificConclusion(@RequestParam String regNumber) throws NoConclusionException,
            UserNotFoundException {
        ConclusionDto conclusionDto = conclusionService.getSpecific(regNumber);
        return ResponseEntity.ok(conclusionDto);
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
            throws UserNotFoundException, RegionNotFoundException, CaseNotFound {
        conclusionService.saveConclusion(createConclusionRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Document successfully saved.");
    }

    @Operation(summary = "Edit a temporary conclusion", description = "After saving user can continue to edit it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited a temporary conclusion")
    })
    @PutMapping("/edit")
    public ResponseEntity<?> edit(@RequestParam String registrationNumber,
                                  @RequestParam String UD,
                                  @RequestParam(required = false) String iinOfCalled,
                                  @RequestParam(required = false) String BIN,
                                  @RequestParam(required = false) String jobTitle,
                                  @RequestParam String region,
                                  @RequestParam(required = false) String plannedActions,
                                  @RequestParam(required = false) LocalDateTime eventDateTime,
                                  @RequestParam(required = false) String eventPlace,
                                  @RequestParam(required = false) String relation,
                                  @RequestParam(required = false) String investigationType,
                                  @RequestParam(required = false) String relatesToBusiness,
                                  @RequestParam String iinOfInvestigator,
                                  @RequestParam(required = false) String iinDefender,
                                  @RequestParam(required = false) String justification,
                                  @RequestParam(required = false) String result)
            throws UserNotFoundException, RegionNotFoundException, NoTemporaryConclusionFound, CaseNotFound {
        CreateConclusionRequest createConclusionRequest = new CreateConclusionRequest();
        createConclusionRequest.setUD(UD);
        createConclusionRequest.setRegion(region);
        createConclusionRequest.setIINOfCalled(iinOfCalled);
        createConclusionRequest.setJobTitle(jobTitle);
        createConclusionRequest.setBIN_IIN(BIN);
        createConclusionRequest.setEventPlace(eventPlace);
        createConclusionRequest.setJustification(justification);
        createConclusionRequest.setEventDateTime(eventDateTime);
        createConclusionRequest.setIINDefender(iinDefender);
        createConclusionRequest.setResult(result);
        createConclusionRequest.setIINOfInvestigator(iinOfInvestigator);
        createConclusionRequest.setRelation(relation);
        createConclusionRequest.setInvestigationType(investigationType);
        createConclusionRequest.setPlannedActions(plannedActions);
        createConclusionRequest.setRelatesToBusiness(relatesToBusiness);

        EditSavedConclusionRequest editSavedConclusionRequest = new EditSavedConclusionRequest();
        editSavedConclusionRequest.setRegistrationNumber(registrationNumber);
        editSavedConclusionRequest.setCreateConclusionRequest(createConclusionRequest);

        conclusionService.editSavedConclusion(editSavedConclusionRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Document successfully edited.");
    }


    @Operation(summary = "Making decision for conclusion", description = " «На согласовании», «Согласовано», «Отказано», «Оставлено без рассмотрения», «Отправлено на доработку».")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Decision was made")
    })
    @PostMapping("/decision")
    public ResponseEntity<?> decision(@RequestBody DecisionRequest decisionRequest) throws UserNotFoundException, NoConclusionException {
        conclusionService.makeDecision(decisionRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Decision was made.");
    }


    @Operation(summary = "History call")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History retrieved")
    })
    @GetMapping("/history")
    public ResponseEntity<History> history(@RequestParam String iinOfCalled,
                                           @RequestParam String goal) throws UserNotFoundException {
        History history = conclusionService.history(iinOfCalled, goal);
        return ResponseEntity.ok(history);
    }
}
