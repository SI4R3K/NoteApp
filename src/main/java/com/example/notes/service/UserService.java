package com.example.notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.notes.model.Users;
import com.example.notes.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    UserRepo repo;

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
}
