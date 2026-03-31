package com.example.notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.notes.exception.NoteNotFoundException;
import com.example.notes.model.Notes;
import com.example.notes.model.Users;
import com.example.notes.repository.NotesRepository;

@Service
public class NotesService {

    private final AuthServive authService;

    @Autowired
    NotesRepository repo;

    public NotesService(AuthServive authServive) {
        this.authService = authServive;
    }

    public Notes addNote(Notes notes) {
        Users user = authService.getCurrentUser();

        notes.setUser(user);

        return repo.save(notes);
    }

    public List<Notes> getAllMyNotes() {

        Users user = authService.getCurrentUser();

        return this.repo.findByUser(user);
    }

    public Notes getMyNotesById(Long id) {

        Users user = authService.getCurrentUser();

        return repo.findByIdAndUser(id,user)
                .orElseThrow(() -> new NoteNotFoundException(id));
    }

    public void deleteNotesById(Long id) {
        Users user = authService.getCurrentUser();

        Notes note = repo.findByIdAndUser(id, user)
                    .orElseThrow(()-> new NoteNotFoundException(id));

        repo.delete(note);
    }

    public Notes updateNotes(Long id, Notes updatedNotes) {

        Users user = authService.getCurrentUser();

        Notes note = repo.findByIdAndUser(id,user)
                    .orElseThrow(() -> new NoteNotFoundException(id));
        
        note.setTitle(updatedNotes.getTitle());
        note.setContent(updatedNotes.getContent());
        note.markUpdated();

        return repo.save(note);
    }

    public List<Notes> searchByTitle(String title) {

        Users user = authService.getCurrentUser();

       List<Notes> notes = repo.findByTitleAndUser(title,user);
        
        if (notes.isEmpty()) {
            throw new NoteNotFoundException(title);
        }

        return notes;
    }

    public List<Notes> getAllNotes() {
        return this.repo.findAll();
    }
}