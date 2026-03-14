package com.example.notes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.notes.exception.NoteNotFoundException;

@Service
public class NotesService {

    private List<Notes> notesList = new ArrayList<>();
    private int nextId = 1;

    public Notes addNote(Notes notes) {
        notes.setId(nextId++);
        notesList.add(notes);
        return notes;
    }

    public List<Notes> getAll() {
        return notesList;
    }

    public Notes getNotesById(int id) {
        for (Notes note : notesList) {
            if (note.getId() == id) {
                return note;
            }
        }
        throw new NoteNotFoundException(id);
    }

    public boolean deleteNotesById(int id) {
        return notesList.removeIf(note -> note.getId() == id);
    }

    public Notes updateNotes(int id, Notes updatedNotes) {

        Notes note = getNotesById(id);

        if (note == null) {
            throw new NoteNotFoundException(id);
        }

        note.setTitle(updatedNotes.getTitle());
        note.setContent(updatedNotes.getContent());
        note.markUpdated();

        return note;
    }

    public List<Notes> searchByTitle(String title) {
        List<Notes> searchNotes = new ArrayList<>();
        for (Notes not : this.notesList) {
            if (not.getTitle().equals(title)) {
                searchNotes.add(not);
            }
        }
        
        if (searchNotes.isEmpty()) {
            throw new NoteNotFoundException(title);
        }

        return searchNotes;
    }
}