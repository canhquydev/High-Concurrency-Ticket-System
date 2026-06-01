package com.quy.highconcurrency_ticket_system.service;

import com.quy.highconcurrency_ticket_system.dto.request.UserRequest;
import com.quy.highconcurrency_ticket_system.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse create(UserRequest request);
    List<UserResponse> index();
    UserResponse findById(Long id);
    UserResponse update(Long id, UserRequest request);
    void delete(Long id);
}
