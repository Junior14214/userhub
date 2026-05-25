package com.github.ailton.userhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectRequest(
    
        @NotBlank(message = "Name is required")
        @Size(max = 200, message = "Project id must be at most 200 characters")
        String id,

        @NotBlank(message = "Name is required")
        @Size(max = 120, message = "Project name must be at most 120 characters")
        String name

        ) {

}
