package com.example.demo.controllers;

import com.example.demo.dtos.requests.*;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/profile")
    public ResponseEntity<UserDto> profile(@RequestParam String email)
            throws UserNotFoundException {
        UserDto userProf = userService.getProfile(email);
        return ResponseEntity.status(HttpStatus.OK).body(userProf);
    }

    @Operation(summary = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password format"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/password")
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
    @PostMapping("/reset-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest)
            throws UserNotFoundException {
        userService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Password reset link sent to your email.");
    }

    @Operation(summary = "All names")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All names retrieved")
    })
    @GetMapping("/allNames")
    public ResponseEntity<List<String>> allNames(){
        List<String> allNames = userService.allNames();
        return ResponseEntity.ok(allNames);
    }

    @Operation(summary = "All users within same department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users retrieved")
    })
    @GetMapping("/depRelated")
    public ResponseEntity<List<UserDto>> depRelated(@RequestParam String department){
        List<UserDto> allUsers = userService.getAllWithinDep(department);
        return ResponseEntity.ok(allUsers);
    }

    @Operation(summary = "All users that has same job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users retrieved")
    })
    @GetMapping("/jobRelated")
    public ResponseEntity<List<UserDto>> jobRelated(@RequestParam String jobTitle){
        List<UserDto> allUsers = userService.getAllWithinJob(jobTitle);
        return ResponseEntity.ok(allUsers);
    }

    @Operation(summary = "Edit profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile successfully edited."),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/editProfile")
    public ResponseEntity<?> editProfiled(@RequestBody EditProfileRequest editProfileRequest)
            throws UserNotFoundException {
        userService.editProfile(editProfileRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Profile has successfully edited.");
    }

    @Operation(summary = "All users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users retrieved")
    })
    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(userDtos);
    }


}
