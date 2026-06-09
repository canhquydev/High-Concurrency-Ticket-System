package com.quy.highconcurrency_ticket_system.service;

import com.nimbusds.jose.JOSEException;
import com.quy.highconcurrency_ticket_system.dto.request.IntrospectTokenRq;
import com.quy.highconcurrency_ticket_system.dto.request.LoginRequest;
import com.quy.highconcurrency_ticket_system.dto.request.RegisterRequest;
import com.quy.highconcurrency_ticket_system.dto.response.IntrospectTokenRp;
import com.quy.highconcurrency_ticket_system.dto.response.LoginResponse;
import com.quy.highconcurrency_ticket_system.dto.response.UserResponse;

import java.text.ParseException;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    IntrospectTokenRp verify(IntrospectTokenRq request) throws ParseException, JOSEException;
}
