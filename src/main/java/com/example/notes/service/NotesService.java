package com.example.notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.notes.exception.NoteNotFoundException;
import com.example.notes.model.Notes;
import com.example.notes.repository.NotesRepository;

@Service
public class NotesService {

    @Autowired
    NotesRepository repo;

    public Notes addNote(Notes notes) {
        return repo.save(notes);
    }

    public List<Notes> getAll() {
        return this.repo.findAll();
    }

    public Notes getNotesById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
    }

    public void deleteNotesById(int id) {
        if (!repo.existsById(id)) {
            throw new NoteNotFoundException(id);
        }

        repo.deleteById(id);
    }

    public Notes updateNotes(int id, Notes updatedNotes) {

        Notes note = repo.findById(id)
                    .orElseThrow(() -> new NoteNotFoundException(id));
        
        note.setTitle(updatedNotes.getTitle());
        note.setContent(updatedNotes.getContent());
        note.markUpdated();

        return repo.save(note);
    }

    public List<Notes> searchByTitle(String title) {

        List<Notes> notes = repo.findByTitle(title);
        
        if (notes.isEmpty()) {
            throw new NoteNotFoundException(title);
        }

        return notes;
    }
}