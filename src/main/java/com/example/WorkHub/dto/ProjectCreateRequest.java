package com.example.WorkHub.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectCreateRequest(
    @NotBlank(message = "Project name cannot be empty")
    String name,
    
    @NotBlank(message = "Creator email cannot be empty")
    String createdBy
) {}
