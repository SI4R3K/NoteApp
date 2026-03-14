package com.example.notes;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping("/notes")
    public Notes createNote(@RequestBody Notes notes) {
        return notesService.addNotes(notes);
    }

    @GetMapping("/notes")
    public List<Notes> getAllNotes() {
        return notesService.getAll();
    }

    @GetMapping("/notes/{id}")
    public Notes getNotes(@PathVariable int id) {
        return notesService.getNotesById(id);
    }

    @DeleteMapping("/notes/{id}")
    public boolean deleteNotesById(@PathVariable int id) {
        return notesService.deleteNotesById(id);
    }
    
}
