package com.example.WorkHub.controller;

import com.example.WorkHub.dto.TaskUpdateRequest;
import com.example.WorkHub.model.Task;
import com.example.WorkHub.service.TaskService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Patch instead of Post or Put since we're updating a specific field :D
    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable UUID id,
            @Valid @RequestBody TaskUpdateRequest request) {

        Task updatedTask = taskService.updateTaskStatus(id, request.status());
        return new ResponseEntity<>(updatedTask, HttpStatus.ACCEPTED);
    }
}
