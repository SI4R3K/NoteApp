package com.example.notes.exception;

import java.util.ArrayList;
import java.util.List;

public class RegistrationConflictException extends RuntimeException {

    private final List<String> errors = new ArrayList<>();

    public RegistrationConflictException(boolean usernameTaken, boolean emailTaken, String username, String email) {

        if (usernameTaken) {
            errors.add("Username " + username + " already exists");
        }

        if (emailTaken) {
            errors.add("Email " + email + " is already registered");
        }
    }

    @Override
    public String getMessage() {
        return String.join(" | ", errors);
    }

    public List<String> getErrors() {
        return errors;
    }
}