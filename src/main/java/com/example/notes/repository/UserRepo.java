package com.example.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notes.model.Users;


public interface UserRepo extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}
