package com.example.notes.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.notes.model.Users;
import com.example.notes.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UsersControllerLoginTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UsersController usersController;

    @Test
    void whenValidUser_thenReturnsJwtTokenString() {

        Users user = new Users();
        user.setUsername("john");
        user.setPassword("password");

        // mock JWT token returned by service
        when(userService.verify(user)).thenReturn("mock-jwt-token");

        String response = usersController.login(user);

        assertEquals("mock-jwt-token", response);
    }

    @Test
    void whenInvalidUser_thenReturnsLoginFailed() {

        Users user = new Users();
        user.setUsername("john");
        user.setPassword("wrong");

        when(userService.verify(user)).thenReturn("Login failed");

        String response = usersController.login(user);

        assertEquals("Login failed", response);
    }
}