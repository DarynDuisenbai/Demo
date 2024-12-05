package com.example.demo.controller;


import com.example.demo.service.spec.ActionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Tag(name = "Actions", description = "API for managing actions")
public class ActionController {
    private final ActionService actionService;

    @Operation(summary = "All actions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All actions returned successfully"),
    })
    @GetMapping("/allActions")
    public ResponseEntity<List<String>> allActions(){
        List<String> allActions = actionService.allActions();
        return ResponseEntity.ok(allActions);
    }
}
