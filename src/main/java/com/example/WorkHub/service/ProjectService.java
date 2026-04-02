package com.example.WorkHub.service;

import com.example.WorkHub.model.Project;
import com.example.WorkHub.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Project createProject(String name, String createdByEmail) {
        Project project = new Project();
        project.setName(name);
        project.setCreatedBy(createdByEmail);
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            throw new RuntimeException("No projects were found");
        }
        return projects;
    }

    @Transactional(readOnly = true)
    public Project getProjectById(UUID id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }
}
