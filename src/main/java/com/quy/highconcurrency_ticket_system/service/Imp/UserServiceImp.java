package com.quy.highconcurrency_ticket_system.service.Imp;

import com.quy.highconcurrency_ticket_system.dto.request.UserUpdateRq;
import com.quy.highconcurrency_ticket_system.enums.Role;
import com.quy.highconcurrency_ticket_system.exception.DuplicateResourceException;
import com.quy.highconcurrency_ticket_system.exception.ResourceNotFoundException;
import com.quy.highconcurrency_ticket_system.dto.request.UserRequest;
import com.quy.highconcurrency_ticket_system.dto.response.UserResponse;
import com.quy.highconcurrency_ticket_system.model.User;
import com.quy.highconcurrency_ticket_system.repository.UserRepository;
import com.quy.highconcurrency_ticket_system.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse create(UserRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Email", request.getEmail());
        }
        User user = User.builder()
                .email(request.getEmail())
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .password(request.getPassword())
                .build();
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public List<UserResponse> index() {
        List<User> userList = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for(User u: userList){
            userResponses.add(new UserResponse(u));
        }
        return userResponses;
    }

    public UserResponse findById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User", "id", id);
        }
        return new UserResponse(user.get());
    }

    @Override
    public UserResponse update(Long id, UserUpdateRq request) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User", "id", id);
        }
        User userUpdate = user.get();
        if(!request.getEmail().equals(userUpdate.getEmail())){
            if(userRepository.existsByEmail(request.getEmail())){
                throw new DuplicateResourceException("Email", request.getEmail());
            }
        }
        if(request.getEmail() != null){
            userUpdate.setEmail(request.getEmail());
        }
        if(request.getPassword() != null){
            userUpdate.setPassword(request.getPassword());
        }
        if(request.getRole() != null){
            userUpdate.setRole(Role.valueOf(request.getRole().toUpperCase()));
        }

        userRepository.save(userUpdate);
        return new UserResponse(userUpdate);
    }

    @Override
    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User", "id", id);
        }
        User userDelete = user.get();
        userDelete.setDeleted(true);
        userRepository.save(userDelete);
    }
}
