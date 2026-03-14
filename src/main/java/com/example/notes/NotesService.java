package com.example.notes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NotesService {

    private List<Notes> notesList = new ArrayList<>();
    private int nextId = 1;

    public Notes addNotes(Notes notes) {
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
        return null;
    }

    public boolean deleteNotesById(int id) {
        return notesList.removeIf(note -> note.getId() == id);
    }
}