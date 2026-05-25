package com.github.ailton.userhub.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.github.ailton.userhub.domain.User;
import com.github.ailton.userhub.dto.UserRequest;
import com.github.ailton.userhub.dto.UserResponse;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
