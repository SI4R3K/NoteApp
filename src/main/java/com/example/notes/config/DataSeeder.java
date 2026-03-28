package com.example.notes.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.notes.model.Users;
import com.example.notes.repository.UserRepo;

@Configuration
public class DataSeeder {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Bean
    CommandLineRunner seedUsers(UserRepo userRepo) {
        return args -> {
            if (userRepo.count() == 0) {
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(encoder.encode("admin123"));

                Users bob = new Users();
                bob.setUsername("bob");
                bob.setEmail("bob@example.com");
                bob.setPassword(encoder.encode("bob123"));

                userRepo.save(admin);
                userRepo.save(bob);
            }
        };
    }
        }
