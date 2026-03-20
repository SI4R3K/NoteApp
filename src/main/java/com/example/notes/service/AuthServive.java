package com.example.notes.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.notes.model.Users;
import com.example.notes.repository.UserRepo;

@Service
public class AuthServive {
    
    private final UserRepo userRepo;

    public AuthServive(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Users user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                "Profile with provided username " + username + " was not found"
            );
        }

        return user;
    }
    
}
