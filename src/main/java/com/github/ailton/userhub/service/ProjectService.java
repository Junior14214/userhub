package com.github.ailton.userhub.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.ailton.userhub.domain.ExternalProject;
import com.github.ailton.userhub.domain.ExternalProjectId;
import com.github.ailton.userhub.domain.User;
import com.github.ailton.userhub.dto.ProjectRequest;
import com.github.ailton.userhub.dto.ProjectResponse;
import com.github.ailton.userhub.exception.BusinessException;
import com.github.ailton.userhub.exception.ResourceNotFoundException;
import com.github.ailton.userhub.mapper.ProjectMapper;
import com.github.ailton.userhub.repository.ExternalProjectRepository;
import com.github.ailton.userhub.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ExternalProjectRepository externalProjectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponse addProject(String userId, ProjectRequest request){
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ExternalProjectId projectId = new ExternalProjectId(request.id(), userId);
        if(externalProjectRepository.existsById(projectId)){
            throw new BusinessException("Project already exists for this user");
        }

        ExternalProject project = projectMapper.toEntity(request, user);
        return projectMapper.toResponse(externalProjectRepository.save(project));
    }

    public List<ProjectResponse> findAllByUserId(String userId) {
        if(!userRepository.existsById(userId)){
            throw new BusinessException("User not found");
        }

        return externalProjectRepository.findAllById_UserId(userId)
        .stream()
        .map(projectMapper::toResponse)
        .toList();
    }

}
