package com.example.demo.controllers;

import com.example.demo.dtos.requests.ChangePasswordRequest;
import com.example.demo.dtos.requests.DeleteAccountRequest;
import com.example.demo.dtos.requests.ForgotPasswordRequest;
import com.example.demo.dtos.requests.GetProfileRequest;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.exceptions.InvalidPasswordException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> profile(@RequestBody GetProfileRequest getProfileRequest)
            throws UserNotFoundException {
        UserDto userProf = userService.getProfile(getProfileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userProf);
    }

    @PostMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest)
            throws UserNotFoundException, InvalidPasswordException {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestBody DeleteAccountRequest deleteAccountRequest)
            throws UserNotFoundException {
        userService.deleteAccount(deleteAccountRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User has successfully deleted.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest)
            throws UserNotFoundException {
        userService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Password reset link sent to your email.");
    }
}
