package com.github.ailton.userhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.ailton.userhub.domain.ExternalProject;
import com.github.ailton.userhub.domain.ExternalProjectId;

@Repository
public interface ExternalProjectRepository extends JpaRepository<ExternalProject, ExternalProjectId> {
    
    List<ExternalProject> findAllById_UserId(String userId);

}
