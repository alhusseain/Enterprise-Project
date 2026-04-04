package com.example.WorkHub.dto;

import com.example.WorkHub.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TaskUpdateRequest(
                @NotBlank(message = "Task status cannot be empty") @Pattern(regexp = "^(P|IP|C)$", message = "Status must be P (PENDING), IP (IN PROGRESS), or C (COMPLETED)") TaskStatus status) {
}
