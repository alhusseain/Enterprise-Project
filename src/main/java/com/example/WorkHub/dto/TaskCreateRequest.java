package com.example.WorkHub.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskCreateRequest(
    @NotBlank(message = "Task title cannot be empty")
    String title
) {}
