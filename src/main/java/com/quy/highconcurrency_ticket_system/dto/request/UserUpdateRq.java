package com.quy.highconcurrency_ticket_system.dto.request;

import com.quy.highconcurrency_ticket_system.anotation.EnumValid;
import com.quy.highconcurrency_ticket_system.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRq {
    private String fullName;
    @Email
    private String email;
    private String password;
    @EnumValid(enumClass = Role.class, message = "Role is not valid")
    private String role;
}
