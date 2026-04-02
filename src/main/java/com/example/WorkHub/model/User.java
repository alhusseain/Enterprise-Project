package com.example.WorkHub.model;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(unique = true)
    private String email;
    private String password;
    private String roles;
    private Long tenantId;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(@Nullable String encode) {
        password = encode;
    }

    public @Nullable String getPassword() {
        return password;
    }
}
