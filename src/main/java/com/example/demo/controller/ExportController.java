package com.example.demo.controller;

import com.example.demo.service.impl.ExportServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Tag(name = "Export", description = "API for managing export")
public class ExportController {
    private final ExportServiceImpl exportService;

    @Operation(summary = "Get excel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Excel created successfully")
    })
    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam String IIN) {
        byte[] data = exportService.exportToExcel(IIN).readAllBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=conclusions.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }


    @Operation(summary = "Get pdf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pdf created successfully")
    })
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportToPdf(@RequestParam String IIN) {
        byte[] data = exportService.exportToPdf(IIN).readAllBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=conclusions.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }
}
