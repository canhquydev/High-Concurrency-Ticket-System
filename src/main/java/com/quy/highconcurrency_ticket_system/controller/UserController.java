package com.quy.highconcurrency_ticket_system.controller;

import com.quy.highconcurrency_ticket_system.dto.request.UserRequest;
import com.quy.highconcurrency_ticket_system.dto.request.UserUpdateRq;
import com.quy.highconcurrency_ticket_system.dto.response.APIResponse;
import com.quy.highconcurrency_ticket_system.dto.response.UserResponse;
import com.quy.highconcurrency_ticket_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<UserResponse>>> index(){
        APIResponse<List<UserResponse>> response = new APIResponse<>(HttpStatus.OK.value(), "Users retrieved successfully"
                , userService.index(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponse>> findById(@PathVariable Long id){
        APIResponse<UserResponse> response = new APIResponse<>(HttpStatus.OK.value(), "User retrieved successfully"
                , userService.findById(id), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserResponse>> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRq request){
        APIResponse<UserResponse> response = new APIResponse<>(HttpStatus.OK.value(), "User update successfully"
                , userService.update(id, request), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Object>> delete(@PathVariable Long id){
        userService.delete(id);
        APIResponse<Object> response = new APIResponse<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully", null, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
