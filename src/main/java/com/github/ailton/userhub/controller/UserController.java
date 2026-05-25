package com.github.ailton.userhub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ailton.userhub.dto.UserRequest;
import com.github.ailton.userhub.dto.UserResponse;
import com.github.ailton.userhub.dto.UserUpdateRequest;
import com.github.ailton.userhub.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(request));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.update(id, request));

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {

        return ResponseEntity.ok(userService.findById(id));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> delete(@PathVariable String id) {

        userService.delete(id);
        return ResponseEntity.noContent().build();

    }

}
