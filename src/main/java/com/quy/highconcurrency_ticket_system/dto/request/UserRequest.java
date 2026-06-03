package com.quy.highconcurrency_ticket_system.dto.request;

import com.quy.highconcurrency_ticket_system.anotation.EnumValid;
import com.quy.highconcurrency_ticket_system.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    @EnumValid(enumClass = Role.class, message = "Role is not valid")
    private String role;
}
