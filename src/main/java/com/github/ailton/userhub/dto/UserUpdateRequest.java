package com.github.ailton.userhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    
        @Size(max = 120, message = "Name must be at most 120 characters")
        String name,

        @Size(max = 200, message = "Name must be at most 200 characters")
        @Email(message = "Email must be valid")
        String email,

        @Size(min = 6, max = 129, message = "Password must be between 6 and 129 characters")
        String password

        ) {

}
