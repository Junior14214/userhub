package com.github.ailton.userhub.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ailton.userhub.controller.UserController;
import com.github.ailton.userhub.exception.BusinessException;
import com.github.ailton.userhub.exception.ResourceNotFoundException;
import com.github.ailton.userhub.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.github.ailton.userhub.dto.UserRequest;
import com.github.ailton.userhub.dto.UserResponse;
import com.github.ailton.userhub.dto.UserUpdateRequest;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest("Ailton", "ailton@email.com", "123456");

        userResponse = new UserResponse(
                "uuid-123",
                "Ailton",
                "ailton@email.com",
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create user and return 201")
    @WithMockUser
    void shouldCreateUserAndReturn201() throws Exception {
        when(userService.create(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("uuid-123"))
                .andExpect(jsonPath("$.email").value("ailton@email.com"));
    }

    @Test
    @DisplayName("Should return 400 when request is invalid")
    @WithMockUser
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        UserRequest invalidRequest = new UserRequest("", "email-invalido", "123");

        mockMvc.perform(post("/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should find user by id and return 200")
    @WithMockUser
    void shouldFindUserByIdAndReturn200() throws Exception {
        when(userService.findById("uuid-123")).thenReturn(userResponse);

        mockMvc.perform(get("/users/uuid-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("uuid-123"));
    }

    @Test
    @DisplayName("Should return 404 when user not found")
    @WithMockUser
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(userService.findById("uuid-inexistente"))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/users/uuid-inexistente"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete user and return 204")
    @WithMockUser
    void shouldDeleteUserAndReturn204() throws Exception {
        mockMvc.perform(delete("/users/uuid-123")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should update user and return 200")
    @WithMockUser
    void shouldUpdateUserAndReturn200() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest("Ailton Silva", null, null);

        when(userService.update(any(String.class), any(UserUpdateRequest.class)))
                .thenReturn(userResponse);

        mockMvc.perform(patch("/users/uuid-123")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("uuid-123"));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent user")
    @WithMockUser
    void shouldReturn404WhenUpdatingNonExistentUser() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest("Ailton Silva", null, null);

        when(userService.update(any(String.class), any(UserUpdateRequest.class)))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(patch("/users/uuid-inexistente")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 409 when updating with email already in use")
    @WithMockUser
    void shouldReturn409WhenUpdatingWithEmailAlreadyInUse() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest(null, "outro@email.com", null);

        when(userService.update(any(String.class), any(UserUpdateRequest.class)))
                .thenThrow(new BusinessException("Email already in use"));

        mockMvc.perform(patch("/users/uuid-123")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isConflict());
    }
}
