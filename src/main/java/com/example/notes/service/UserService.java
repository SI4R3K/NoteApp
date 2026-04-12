package com.example.notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.notes.exception.RegistrationConflictException;
import com.example.notes.model.Users;
import com.example.notes.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user) {

        boolean usernameTaken = repo.existsByUsername(user.getUsername());
        boolean emailTaken = repo.existsByEmail(user.getEmail());

        if (usernameTaken || emailTaken) {
            throw new RegistrationConflictException(
                usernameTaken, 
                emailTaken, 
                user.getUsername(), 
                user.getEmail()
            );
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public List<Users> getAll() {
        return repo.findAll();
    }

    public Users getUserByUsername(String username) {

        Users user = repo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                "Profile with provided username " + username + " was not found"
            );
        }

        return repo.findByUsername(username);
    }

    public String verify(Users user) {
        Authentication authentication = 
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        
            if(authentication.isAuthenticated()) {
                return jwtService.generateToken(user.getUsername());
            }

        return "Login failed";
    }
}
