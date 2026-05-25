package com.github.ailton.userhub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ailton.userhub.dto.ProjectRequest;
import com.github.ailton.userhub.dto.ProjectResponse;
import com.github.ailton.userhub.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/{userId}/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> addProject(@PathVariable String userId, @RequestBody @Valid ProjectRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.addProject(userId, request));

    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> findAllByUserId(@PathVariable String userId) {

        return ResponseEntity.ok(projectService.findAllByUserId(userId));

    }
    
}
