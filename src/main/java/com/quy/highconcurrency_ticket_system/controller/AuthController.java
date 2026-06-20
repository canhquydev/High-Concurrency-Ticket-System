package com.quy.highconcurrency_ticket_system.controller;

import com.nimbusds.jose.JOSEException;
import com.quy.highconcurrency_ticket_system.dto.request.IntrospectTokenRq;
import com.quy.highconcurrency_ticket_system.dto.request.LoginRequest;
import com.quy.highconcurrency_ticket_system.dto.request.RegisterRequest;
import com.quy.highconcurrency_ticket_system.dto.response.APIResponse;
import com.quy.highconcurrency_ticket_system.dto.response.IntrospectTokenRp;
import com.quy.highconcurrency_ticket_system.dto.response.LoginResponse;
import com.quy.highconcurrency_ticket_system.dto.response.UserResponse;
import com.quy.highconcurrency_ticket_system.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Authentication APIs")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @Operation(summary = "Register user", description = "Register a new user in the system")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request){
        APIResponse<UserResponse> response = new APIResponse<>(200, "User created successfully", service.register(request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Login user", description = "Authenticate a user and return a token")
    @ApiResponse(responseCode = "200", description = "User logged in successfully")
    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request){
        APIResponse<LoginResponse> response = new APIResponse<>(200, "Login successfully", service.login(request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Verify token", description = "Verify the validity of a given token")
    @ApiResponse(responseCode = "200", description = "Token verified successfully")
    @PostMapping("/verify")
    public ResponseEntity<APIResponse<IntrospectTokenRp>> verify(@RequestBody IntrospectTokenRq request) throws ParseException, JOSEException {
        APIResponse<IntrospectTokenRp> response = new APIResponse<>(200, "Verify successfully", service.verify(request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
