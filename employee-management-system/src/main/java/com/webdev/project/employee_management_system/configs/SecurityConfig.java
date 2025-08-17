package com.webdev.project.employee_management_system.configs;

import com.webdev.project.employee_management_system.filters.JwtAuthenticationFilter;
import com.webdev.project.employee_management_system.services.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
                // CORS setup
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                }))
                .csrf(csrf -> csrf.disable())

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()

                        // ADMIN-only endpoints
                        .requestMatchers(HttpMethod.POST, "/api/departments/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/departments/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/departments/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/employees/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasAuthority("ADMIN")

                        // HR_MANAGER endpoints
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").hasAnyAuthority("HR_MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/leave-requests/**").hasAnyAuthority("HR_MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/leave-requests/**").hasAnyAuthority("HR_MANAGER", "ADMIN")

                        // EMPLOYEE endpoints
                        .requestMatchers(HttpMethod.POST, "/api/leave-requests/**").hasAnyAuthority("EMPLOYEE", "HR_MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/leave-requests/my/**").hasAuthority("EMPLOYEE")

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
