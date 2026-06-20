package com.quy.highconcurrency_ticket_system.controller;

import com.quy.highconcurrency_ticket_system.dto.request.UserUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.APIResponse;
import com.quy.highconcurrency_ticket_system.dto.response.UserResponse;
import com.quy.highconcurrency_ticket_system.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api/v1/admin/users")
@Slf4j
@Tag(name = "User", description = "User Management APIs")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping
    public ResponseEntity<APIResponse<List<UserResponse>>> index(){
        APIResponse<List<UserResponse>> response = new APIResponse<>(HttpStatus.OK.value(), "Users retrieved successfully"
                , userService.index(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponse>> findById(@PathVariable Long id){
        APIResponse<UserResponse> response = new APIResponse<>(HttpStatus.OK.value(), "User retrieved successfully"
                , userService.findById(id), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update a user", description = "Update an existing user by their ID")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponse>> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRq request){
        APIResponse<UserResponse> response = new APIResponse<>(HttpStatus.OK.value(), "User update successfully"
                , userService.update(id, request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete a user", description = "Delete a user by their ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> delete(@PathVariable Long id){
        userService.delete(id);
        APIResponse<Object> response = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully", null, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
