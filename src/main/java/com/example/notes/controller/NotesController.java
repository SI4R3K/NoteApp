package com.example.notes.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notes.model.Notes;
import com.example.notes.service.NotesService;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("notes")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping
    public Notes createMyNote(@RequestBody Notes notes) {
        return notesService.addNote(notes);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public List<Notes> getAllMyNotes() {
        return notesService.getAllMyNotes();
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Notes getNotes(@PathVariable Long id) {
        return notesService.getMyNotesById(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/search")
    public List<Notes> getNotesByTitle(@RequestParam String title) {
        return notesService.searchByTitle(title);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{id}")
    public void deleteMyNotesById(@PathVariable Long id) {
        notesService.deleteMyNotesById(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Notes updateNotes(@PathVariable Long id, @RequestBody Notes updatedNotes) {
        return notesService.updateNotes(id, updatedNotes);
    }
    
}
