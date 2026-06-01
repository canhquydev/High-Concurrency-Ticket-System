package com.quy.highconcurrency_ticket_system.dto.response;

import com.quy.highconcurrency_ticket_system.enums.Role;
import com.quy.highconcurrency_ticket_system.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse {
    private Long id;
    private String email;
    private Role role;
    private boolean deleted;
    public UserResponse(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.deleted = user.isDeleted();
    }
}
