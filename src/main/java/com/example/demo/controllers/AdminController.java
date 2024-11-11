package com.example.demo.controllers;


import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @Operation(summary = "Promote user job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job changed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/admin/promote")
    public ResponseEntity<?> promote(@RequestParam String IIN) throws UserNotFoundException {
        userService.promote(IIN);
        return ResponseEntity.status(HttpStatus.OK).body("User successfully promoted.");
    }

}
