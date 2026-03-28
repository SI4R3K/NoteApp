package com.example.notes.exception;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(Long id) {
        super("Note with the title '"+ id + "' was not found");
    }

    public NoteNotFoundException(String title) {
        super("Note with the title '"+ title + "' was not found");
    }
}
