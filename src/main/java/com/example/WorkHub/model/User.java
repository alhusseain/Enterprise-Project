package com.example.WorkHub.model;

import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;
import org.jspecify.annotations.Nullable;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String roles;

    // @TenantId // re-enable this, and set nullable to false when tenant context
    // managing is completed
    @Column(name = "tenant_id", nullable = true)
    private UUID tenantId;

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public @Nullable String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String encode) {
        this.password = encode;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }
}
