package org.example.configuration;

import org.example.repository.UserRepository;
import org.example.service.ApplicationUserDetailsService;
import org.example.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public SecurityConfig(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // or configure as needed
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(POST, "/users/register", "/users/auth", "/users/validate-role").permitAll()
                        .requestMatchers(DELETE, "/users").hasRole("ADMIN")
                        .requestMatchers(POST, "/users").hasRole("ADMIN")
                        .requestMatchers(PATCH, "/users").hasRole("ADMIN")
                        .anyRequest().authenticated()
                ).addFilterBefore(new JwtAuthFilter(jwtService, userDetailsService()),
                        UsernamePasswordAuthenticationFilter.class);;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new ApplicationUserDetailsService(userRepository); // inject repo
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
