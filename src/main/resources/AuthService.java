package com.quy.highconcurrency_ticket_system.service;

import com.quy.highconcurrency_ticket_system.dto.request.LoginRequest;
import com.quy.highconcurrency_ticket_system.dto.request.RegisterRequest;
import com.quy.highconcurrency_ticket_system.dto.response.LoginResponse;
import com.quy.highconcurrency_ticket_system.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
