package com.example.WorkHub.jwt;

import com.example.WorkHub.jwt.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println(request.getRequestURI());
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("token: " + token);
            if (!jwtUtil.validateJwtToken(token)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid bearer token");
                return;
            }

            String email = jwtUtil.getEmailFromToken(token);
            System.out.println("email: " + email);
            System.out.println("in jwt filter");

            var authToken = new UsernamePasswordAuthenticationToken(
                    email, null, List.of() );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        chain.doFilter(request, response);
    }
}
