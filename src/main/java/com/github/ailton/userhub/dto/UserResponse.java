package com.github.ailton.userhub.dto;

import java.time.LocalDateTime;

public record UserResponse(
        String id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        ) {

}
