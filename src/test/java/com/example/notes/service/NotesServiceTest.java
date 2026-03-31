package com.example.notes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.notes.model.Notes;
import com.example.notes.model.Users;
import com.example.notes.repository.NotesRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing Notes Service")
public class NotesServiceTest {


    @Mock
    private AuthServive authServive;
    @Mock
    private NotesRepository repo;

    @InjectMocks
    private NotesService notesService;

    private Users testUser;


    @BeforeEach
    void setUp() {
        this.testUser = new Users();
        this.testUser.setId(1L);
        this.testUser.setUsername("testUser");
    }

    @Test
    @DisplayName("Should successfully add a new note")
    void testAddNoteSuccessfully() {
        // Given
        final Notes note = new Notes();
        note.setTitle("Test note");
        note.setContent("test test test test");

        final Notes savedNote = new Notes();
        savedNote.setId(10L);
        savedNote.setTitle(note.getTitle());
        savedNote.setContent(note.getContent());
        savedNote.setUser(testUser);

        when(authServive.getCurrentUser()).thenReturn(testUser);
        when(repo.save(note)).thenReturn(savedNote);

        // When
        final Notes result = notesService.addNote(note); 

        // Then
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Test note", result.getTitle());
        assertEquals("test test test test", result.getContent());
        assertEquals(testUser, result.getUser());

        verify(authServive, times(1)).getCurrentUser();
        verify(repo, times(1)).save(note);
    }

    @Test
    @DisplayName("Should update note successfully")
    void testUpdateNotesSuccessfully() {
        // Given
        Long noteId = 5L;

        Notes existingNote = new Notes();
        existingNote.setId(noteId);
        existingNote.setTitle("Old title");
        existingNote.setContent("Old content");
        existingNote.setUser(testUser);

        Notes updatedNote = new Notes();
        updatedNote.setTitle("New title");
        updatedNote.setContent("New content");

        Notes savedNote = new Notes();
        savedNote.setId(noteId);
        savedNote.setTitle("New title");
        savedNote.setContent("New content");
        savedNote.setUser(testUser);

        when(authServive.getCurrentUser()).thenReturn(testUser);
        when(repo.findByIdAndUser(noteId, testUser)).thenReturn(Optional.of(existingNote));
        when(repo.save(existingNote)).thenReturn(savedNote);

        // When
        Notes result = notesService.updateNotes(noteId, updatedNote);

        // Then
        assertNotNull(result);
        assertEquals("New title", result.getTitle());
        assertEquals("New content", result.getContent());
        assertEquals(testUser, result.getUser());

        verify(authServive, times(1)).getCurrentUser();
        verify(repo, times(1)).findByIdAndUser(noteId, testUser);
        verify(repo, times(1)).save(existingNote);
    }
}
