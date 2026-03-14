package com.example.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.notes.Notes;
import java.util.List;


@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer>{

    List<Notes> findByTitle(String title);

}
