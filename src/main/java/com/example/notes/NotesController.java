package com.example.notes;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NotesController {

    @GetMapping("/")
    public String showFrom() {
        return "form";
    }

    @PostMapping("/note")
    public String submitNote(Notes notes, Model model) {
        model.addAttribute("notes", notes);
        return "result";
    }
}
