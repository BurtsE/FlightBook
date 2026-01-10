package org.example.configuration;

import org.example.filter.JwtValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtValidationFilter jwtValidationFilter;

    public SecurityConfig(JwtValidationFilter jwtValidationFilter) {
        this.jwtValidationFilter = jwtValidationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // usually disabled for APIs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/hotels", "/hotels/rooms").hasRole("ADMIN")
                        .requestMatchers("/hotels/**", "/hotels/rooms/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtValidationFilter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }
}
