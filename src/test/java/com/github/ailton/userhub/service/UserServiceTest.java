package com.github.ailton.userhub.service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.ailton.userhub.domain.User;
import com.github.ailton.userhub.dto.UserRequest;
import com.github.ailton.userhub.dto.UserResponse;
import com.github.ailton.userhub.dto.UserUpdateRequest;
import com.github.ailton.userhub.exception.BusinessException;
import com.github.ailton.userhub.exception.ResourceNotFoundException;
import com.github.ailton.userhub.mapper.UserMapper;
import com.github.ailton.userhub.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest("Ailton", "ailton@email.com", "123456");

        user = User.builder()
                .id("uuid-123")
                .name("Ailton")
                .email("ailton@email.com")
                .password("hashed_password")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userResponse = new UserResponse(
                "uuid-123",
                "Ailton",
                "ailton@email.com",
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        when(userRepository.existsByEmail(userRequest.email())).thenReturn(false);
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(passwordEncoder.encode(userRequest.password())).thenReturn("hashed_password");
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.create(userRequest);

        assertNotNull(response);
        assertEquals(userResponse.id(), response.id());
        assertEquals(userResponse.email(), response.email());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should throw BusinessException when email already in use")
    void shouldThrowBusinessExceptionWhenEmailAlreadyInUse() {
        when(userRepository.existsByEmail(userRequest.email())).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.create(userRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find user by id successfully")
    void shouldFindUserByIdSuccessfully() {
        when(userRepository.findById("uuid-123")).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.findById("uuid-123");

        assertNotNull(response);
        assertEquals("uuid-123", response.id());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found")
    void shouldThrowResourceNotFoundExceptionWhenUserNotFound() {
        when(userRepository.findById("uuid-inexistente")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.findById("uuid-inexistente"));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        when(userRepository.existsById("uuid-123")).thenReturn(true);

        assertDoesNotThrow(() -> userService.delete("uuid-123"));
        verify(userRepository).deleteById("uuid-123");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentUser() {
        when(userRepository.existsById("uuid-inexistente")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.delete("uuid-inexistente"));
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        UserUpdateRequest updateRequest = new UserUpdateRequest("Ailton Silva", null, null);

        when(userRepository.findById("uuid-123")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user); // ← adicione isso
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.update("uuid-123", updateRequest);

        assertNotNull(response);
        assertEquals("uuid-123", response.id());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent user")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentUser() {
        UserUpdateRequest updateRequest = new UserUpdateRequest("Ailton Silva", null, null);

        when(userRepository.findById("uuid-inexistente")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.update("uuid-inexistente", updateRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessException when updating with email already in use")
    void shouldThrowBusinessExceptionWhenUpdatingWithEmailAlreadyInUse() {
        UserUpdateRequest updateRequest = new UserUpdateRequest(null, "outro@email.com", null);

        when(userRepository.findById("uuid-123")).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("outro@email.com")).thenReturn(true);

        assertThrows(BusinessException.class,
                () -> userService.update("uuid-123", updateRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should not check email when email is not changed")
    void shouldNotCheckEmailWhenEmailIsNotChanged() {
        UserUpdateRequest updateRequest = new UserUpdateRequest(null, "ailton@email.com", null);

        when(userRepository.findById("uuid-123")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user); // ← adicione isso
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.update("uuid-123", updateRequest);

        assertNotNull(response);
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository).save(user);
    }
}
