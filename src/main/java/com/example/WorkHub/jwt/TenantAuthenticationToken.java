package com.example.WorkHub.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class TenantAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private final UUID tenantId;

    public TenantAuthenticationToken(Object principal, Object credentials, UUID tenantId, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.tenantId = tenantId;
    }

    public UUID getTenantId() {
        return tenantId;
    }
}
