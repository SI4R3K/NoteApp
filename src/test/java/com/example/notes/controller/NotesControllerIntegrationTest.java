package com.example.notes.controller;

import com.example.notes.model.Notes;
import com.example.notes.model.Users;
import com.example.notes.repository.NotesRepository;
import com.example.notes.repository.UserRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class NotesControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private NotesRepository notesRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        notesRepository.deleteAll();
        userRepo.deleteAll();

        Users user = new Users();
        user.setUsername("testUser");
        user.setPassword("test123"); // should match your auth logic
        userRepo.save(user);

        Notes note = new Notes();
        note.setTitle("Test Note");
        note.setContent("Test Content");
        note.setUser(user);

        notesRepository.save(note);
    }

    @Test
    void getAllNotes_shouldReturn200() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl + "/notes", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createNote_shouldReturn201() {
        Notes newNote = new Notes();
        newNote.setTitle("New Note");
        newNote.setContent("New Content");

        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl + "/notes", newNote, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void deleteNote_shouldReturn200() {
        Notes note = notesRepository.findAll().get(0);

        ResponseEntity<Void> response =
                restTemplate.exchange(
                        baseUrl + "/notes/" + note.getId(),
                        HttpMethod.DELETE,
                        null,
                        Void.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(notesRepository.existsById(note.getId()));
    }

    @Test
    void getNoteById_shouldReturn200() {
        Notes note = notesRepository.findAll().get(0);

        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl + "/notes/" + note.getId(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getNoteByWrongId_shouldReturn404() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl + "/notes/999999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}