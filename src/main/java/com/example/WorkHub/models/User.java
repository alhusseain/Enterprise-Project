package com.example.WorkHub.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class User {
    @Id
    private String email;
    private String password;
    private String roles;
    private Long tenantId;
}
