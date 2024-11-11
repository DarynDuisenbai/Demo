package com.example.demo.controllers;

import com.example.demo.dtos.requests.CreateUserRequest;
import com.example.demo.dtos.requests.LoginRequest;
import com.example.demo.dtos.responces.TokenInfo;
import com.example.demo.exceptions.*;
import com.example.demo.services.UserService;
import com.example.demo.services.impl.UserServiceImpl;
import com.example.demo.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.internal.authentication.AwsCredentialHelper.LOGGER;

@Controller
@Tag(name = "Auth Controller",  description = "Endpoints for managing authentication")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email or password format")
    })
    public ResponseEntity<?> registerUser(@RequestBody CreateUserRequest createUserRequest)
            throws InvalidLoginException, InvalidPasswordException, DuplicateUserException,
            InvalidUDFormat, InvalidIINFormat {
        userService.register(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and generate tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens generated successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws UserNotFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        final String accessToken = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtTokenUtil.getAccessTokenExpiration())
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtTokenUtil.getRefreshTokenExpiration())
                .build();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString(), refreshTokenCookie.toString())
                .body(tokens);
    }
    @GetMapping("/getInfo")
    @Operation(summary = "Get information from access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information from token received"),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    public ResponseEntity<TokenInfo> getInfo(HttpServletRequest request) {
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token != null) {
            TokenInfo tokenInfo = jwtTokenUtil.getTokenInfo(token);
            return ResponseEntity.ok(tokenInfo);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

}

