package com.example.notes;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("notes")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping
    public Notes createNote(@RequestBody Notes notes) {
        return notesService.addNote(notes);
    }

    @GetMapping
    public List<Notes> getAllNotes() {
        return notesService.getAll();
    }

    @GetMapping("/{id}")
    public Notes getNotes(@PathVariable int id) {
        return notesService.getNotesById(id);
    }

    @GetMapping("/search")
    public List<Notes> getNotesByTitle(@RequestParam String title) {
        return notesService.searchByTitle(title);
    }
    

    @DeleteMapping("/{id}")
    public void deleteNotesById(@PathVariable int id) {
        notesService.deleteNotesById(id);
    }

    @PutMapping("/{id}")
    public Notes updateNotes(@PathVariable int id, @RequestBody Notes updatedNotes) {
        return notesService.updateNotes(id, updatedNotes);
    }
    
}
