package com.example.demo.controllers;

import com.example.demo.dtos.requests.ChangePasswordRequest;
import com.example.demo.dtos.requests.DeleteAccountRequest;
import com.example.demo.dtos.requests.ForgotPasswordRequest;
import com.example.demo.dtos.requests.GetProfileRequest;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.exceptions.InvalidPasswordException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "Endpoints for managing user operations")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
   // @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<UserDto> profile(@RequestBody GetProfileRequest getProfileRequest)
            throws UserNotFoundException {
        UserDto userProf = userService.getProfile(getProfileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userProf);
    }

    @Operation(summary = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password format"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
  //  @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest)
            throws UserNotFoundException, InvalidPasswordException {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
    }

    @Operation(summary = "Delete user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    //@PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestBody DeleteAccountRequest deleteAccountRequest)
            throws UserNotFoundException {
        userService.deleteAccount(deleteAccountRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User has successfully deleted.");
    }

    @Operation(summary = "Forgot password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset link sent to email"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("permitAll")
    @PostMapping("/reset-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest)
            throws UserNotFoundException {
        userService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Password reset link sent to your email.");
    }
}
