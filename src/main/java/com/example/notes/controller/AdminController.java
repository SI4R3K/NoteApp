package com.example.notes.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notes.model.Notes;
import com.example.notes.model.Users;
import com.example.notes.service.NotesService;
import com.example.notes.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final NotesService notesService;

    public AdminController(UserService userService, NotesService notesService) {
        this.userService = userService;
        this.notesService = notesService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allNotes")
    public List<Notes> getAllNotes() {
        return notesService.getAllNotes();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allUsers")
    public List<Users> getAllUsers() {
        return userService.getAll();
    }

}
