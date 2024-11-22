package com.example.demo.controller;


import com.example.demo.dto.responce.AgreementDto;
import com.example.demo.dto.responce.ConclusionDto;
import com.example.demo.exception.AnalystAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.spec.AgreementMapper;
import com.example.demo.mapper.spec.ConclusionMapper;
import com.example.demo.domain.Agreement;
import com.example.demo.domain.Conclusion;
import com.example.demo.service.spec.AgreementService;
import com.example.demo.service.spec.ConclusionService;
import com.example.demo.service.spec.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin", description = "API for managing admin")
public class AdminController {

    private final UserService userService;
    private final ConclusionMapper conclusionMapper;
    private final AgreementMapper agreementMapper;
    private final ConclusionService conclusionService;
    private final AgreementService agreementService;

    @Operation(summary = "Promote user job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job changed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/promote")
    public ResponseEntity<?> promote(@RequestParam String IIN) throws UserNotFoundException, AnalystAlreadyExistsException {
        userService.promote(IIN);
        return ResponseEntity.status(HttpStatus.OK).body("User successfully promoted.");
    }

    @Operation(summary = "Get all conclusions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All conclusions returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/allConclusions")
    public ResponseEntity<List<ConclusionDto>> getAllConclusions(){
        List<Conclusion> conclusions = conclusionService.getAllConclusions();
        List<ConclusionDto> conclusionDtos = conclusionMapper.toDtoList(conclusions);

        return ResponseEntity.ok(conclusionDtos);
    }


    @Operation(summary = "Get all agreements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All agreements returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/allAgreements")
    public ResponseEntity<List<AgreementDto>> getAllAgreements(){
        List<Agreement> agreements = agreementService.getAll();
        List<AgreementDto> agreementDtos = agreementMapper.toDtoList(agreements);

        return ResponseEntity.ok(agreementDtos);
    }
}
