package com.example.demo.controllers;

import com.example.demo.dtos.requests.CreateUserRequest;
import com.example.demo.dtos.requests.LoginRequest;
import com.example.demo.dtos.responces.UserDto;
import com.example.demo.exceptions.InvalidLoginException;
import com.example.demo.exceptions.InvalidPasswordException;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import com.example.demo.services.impl.UserServiceImpl;
import com.example.demo.utils.JwtTokenUtil;
import com.example.demo.utils.Validator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {
    private UserService userService;

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private Validator validator;

    @Autowired
    public AuthController(UserServiceImpl userService,
                          AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil,
                          Validator validator) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.validator = validator;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email or password format")
    })
    public ResponseEntity<?> registerUser(@RequestBody CreateUserRequest createUserRequest) throws InvalidLoginException, InvalidPasswordException {
        userService.register(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and generate tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens generated successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws InvalidLoginException, InvalidPasswordException {
        UserDto userDto = userService.login(loginRequest);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        final String accessToken = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);
    }
}
