package com.example.WorkHub.model;

import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;
import java.util.UUID;

@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String status;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public Job() {
    }

    public UUID getId() { 
        return id; 
    }
    
    public void setId(UUID id) { 
        this.id = id; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public UUID getProjectId() { 
        return projectId; 
    }
    
    public void setProjectId(UUID projectId) { 
        this.projectId = projectId; 
    }
    
    public UUID getTenantId() { 
        return tenantId; 
    }
    
    public void setTenantId(UUID tenantId) { 
        this.tenantId = tenantId; 
    }
}
