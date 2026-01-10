package org.example.configuration;

import jakarta.transaction.Transactional;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    @Transactional
    public CommandLineRunner run(UserRepository userRepository, PasswordEncoder passwordEncoder) throws Exception {
        return args -> {
            if (userRepository.findByUsername("admin").isPresent()) {
                return;
            }
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        };
    }
}
