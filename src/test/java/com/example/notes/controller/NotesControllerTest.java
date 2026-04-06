package com.example.notes.controller;

import com.example.notes.config.JwtFilter;
import com.example.notes.model.Notes;
import com.example.notes.model.Users;
import com.example.notes.repository.NotesRepository;
import com.example.notes.repository.UserRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
public class NotesControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private MockMvc mockMvc;    // Replacing JwtFilter with a mock!!!

    @Autowired
    private UserRepo userRepo;

    @Autowired 
    private NotesRepository notesRepository;

    @MockitoBean
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        Users user = new Users();
        user.setUsername("testUser");
        user.setPassword("testUser123");

        userRepo.save(user);

        Notes note1 = new Notes();
        note1.setTitle("test note");
        note1.setContent("test content");
        note1.setUser(user);

        Notes note2 = new Notes();
        note2.setTitle("test note2");
        note2.setContent("test content2");
        note2.setUser(user);

        notesRepository.save(note1);
        notesRepository.save(note2);
    }

    @Test
    @Order(1)
    void getAllMyNotes_shouldReturn200_whenUserAuthenticated() throws Exception {
        mockMvc.perform(get("/notes")
                .with(user("testUser").roles("USER"))) // pasing @PreAuthorized()
                .andDo(print())
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$", hasSize(2)));
    }
}