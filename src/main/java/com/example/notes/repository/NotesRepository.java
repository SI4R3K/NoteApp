package com.example.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.notes.model.Notes;
import com.example.notes.model.Users;

import java.util.List;
import java.util.Optional;


@Repository
public interface NotesRepository extends JpaRepository<Notes, Long>{

    List<Notes> findByTitle(String title);

    List<Notes> findByUser(Users user);

    Optional<Notes> findByIdAndUser(Long id, Users user);
    List<Notes> findByTitleAndUser(String title, Users user);

}
