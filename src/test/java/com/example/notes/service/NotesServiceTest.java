package com.example.notes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.notes.exception.NoteNotFoundException;
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

    @Test
    @DisplayName("Should delete note successfully")
    void testDeleteMyNotesById() {
        // Given
        Long noteId = 3L;

        Notes noteToDelete = new Notes();
        noteToDelete.setId(noteId);
        noteToDelete.setTitle("note to delete");
        noteToDelete.setContent("this note will be deleted");
        noteToDelete.setUser(testUser);

        when(authServive.getCurrentUser()).thenReturn(testUser);
        when(repo.findByIdAndUser(noteId, testUser)).thenReturn(Optional.of(noteToDelete));

        // When
        notesService.deleteMyNotesById(noteId);

        // Then
        verify(authServive, times(1)).getCurrentUser();
        verify(repo, times(1)).findByIdAndUser(noteId, testUser);
        verify(repo, times(1)).delete(noteToDelete);
    } 

    @Test
    @DisplayName("Should throw NoteNotFoundException when note not found for user")
    void testDeleteMyNotesByIdShouldThrowException() {
        // Given
        Long noteId = 3L;

        when(authServive.getCurrentUser()).thenReturn(testUser);
        when(repo.findByIdAndUser(noteId, testUser)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(NoteNotFoundException.class,
                () -> notesService.deleteMyNotesById(noteId));

        verify(authServive, times(1)).getCurrentUser();
        verify(repo, times(1)).findByIdAndUser(noteId, testUser);

        // delete should NOT happen
        verify(repo, never()).delete(any());
    }

    @Test
    @DisplayName("Should return only notes with specific title")
    void testSearchByTitle() {
        // Given
        Notes goodNote = new Notes();
        goodNote.setId(3L);
        goodNote.setTitle("Good title");
        goodNote.setContent("this note is good");
        goodNote.setUser(testUser);

        Notes badNote = new Notes();
        badNote.setId(4L);
        badNote.setTitle("Bad title");
        badNote.setContent("this note is bad");
        badNote.setUser(testUser);

        List<Notes> notesFound = List.of(goodNote); // only matching title

        when(authServive.getCurrentUser()).thenReturn(testUser);
        when(repo.findByTitleAndUser("Good title", testUser)).thenReturn(notesFound);

        // When
        List<Notes> result = notesService.searchByTitle("Good title");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Good title", result.get(0).getTitle());
        assertEquals(goodNote.getId(), result.get(0).getId());

        verify(authServive, times(1)).getCurrentUser();
        verify(repo, times(1)).findByTitleAndUser("Good title", testUser);
    }

    @Test
    @DisplayName("Should throw NoteNotFoundException if no notes found with title")
    void testSearchByTitleNotFound() {
        // Given
        when(authServive.getCurrentUser()).thenReturn(testUser);
        when(repo.findByTitleAndUser("Good title", testUser)).thenReturn(List.of());

        // When + Then
        assertThrows(NoteNotFoundException.class,
                () -> notesService.searchByTitle("Good title"));

        verify(authServive, times(1)).getCurrentUser();
        verify(repo, times(1)).findByTitleAndUser("Good title", testUser);
    }

    
}

