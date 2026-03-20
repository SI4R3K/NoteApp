package com.example.notes.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notes.model.Users;
import com.example.notes.service.UserService;

@RestController
@RequestMapping("users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{username}")
    public Users getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
}
