package com.github.ailton.userhub.mapper;

import org.springframework.stereotype.Component;

import com.github.ailton.userhub.domain.ExternalProject;
import com.github.ailton.userhub.domain.ExternalProjectId;
import com.github.ailton.userhub.domain.User;
import com.github.ailton.userhub.dto.ProjectRequest;
import com.github.ailton.userhub.dto.ProjectResponse;

@Component
public class ProjectMapper {

    public ExternalProject toEntity(ProjectRequest request, User user) {
        ExternalProjectId id = new ExternalProjectId(request.id(), user.getId());

        return ExternalProject.builder()
        .id(id)
        .name(request.name())
        .user(user)
        .build();
    }

    public ProjectResponse toResponse(ExternalProject project){
        return new ProjectResponse(
            project.getId().getId(),
            project.getId().getUserId(),
            project.getName()
        );
    }
    
}
