package com.springsecurityservice.springsecurityservice.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomUserDTO {
    @Email
    @NonNull
    private String email;
    @Size(min = 6, max = 48)
    private String password;
    @Size(min = 6, max = 48)
    private String confirm;
}
