package com.example.WorkHub.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Tenant {
    @Id
    private long tenantId;
    private String name;
    private String plan;
}
