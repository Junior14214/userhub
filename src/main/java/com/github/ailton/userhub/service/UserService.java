package com.github.ailton.userhub.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ailton.userhub.domain.User;
import com.github.ailton.userhub.dto.UserRequest;
import com.github.ailton.userhub.dto.UserResponse;
import com.github.ailton.userhub.dto.UserUpdateRequest;
import com.github.ailton.userhub.exception.BusinessException;
import com.github.ailton.userhub.exception.ResourceNotFoundException;
import com.github.ailton.userhub.mapper.UserMapper;
import com.github.ailton.userhub.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(UserRequest request) {
        log.info("Creating user with email: {}", request.email());

        if (userRepository.existsByEmail(request.email())) {
            log.warn("Email already in use: {}", request.email());
            throw new BusinessException("Email already in use");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        User saved = userRepository.save(user);
        userRepository.flush();

        log.info("User created successfully with id: {}", user.getId());
        return userMapper.toResponse(userRepository.findById(saved.getId()).orElseThrow());

    }

    @Transactional
    public UserResponse update(String id, UserUpdateRequest request) {
        log.info("Updating user with email: {}", request.email());

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found");
                });

        if (request.email() != null && !user.getEmail().equals(request.email())
                && userRepository.existsByEmail(request.email())) {
            log.warn("Email already in use: {}", request.email());
            throw new BusinessException("Email already in use");
        }

        if (request.name() != null) {
            user.setName(request.name());
        }
        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        log.info("User updated successfully with id: {}", user.getId());
        return userMapper.toResponse(userRepository.save(user));

    }

    @Transactional(readOnly = true)
    public UserResponse findById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void delete(String id) {
        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
             log.warn("User not found with id: {}", id);
            throw new ResourceNotFoundException("User not found");
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }

}
