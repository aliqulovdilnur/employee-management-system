package com.webdev.project.employee_management_system.filters;

import com.webdev.project.employee_management_system.services.CustomUserDetailService;
import com.webdev.project.employee_management_system.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String role = null;
        String token = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);

                // Extract username and role from JWT
                username = jwtUtil.extractUsername(token);
                role = jwtUtil.extractRole(token);

                // Debug logs
                System.out.println("JWT username: " + username);
                System.out.println("JWT role: " + role);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.validateToken(token, username)) {
                    // Set Authentication with authority matching SecurityConfig
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.singleton(new SimpleGrantedAuthority(role)) // plain authority
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("Authentication set successfully for user: " + username);
                    System.out.println("Granted Authorities: " + authToken.getAuthorities());
                } else {
                    System.out.println("Invalid JWT token for user: " + username);
                }
            }

        } catch (Exception e) {
            System.out.println("Error in JWT filter: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
