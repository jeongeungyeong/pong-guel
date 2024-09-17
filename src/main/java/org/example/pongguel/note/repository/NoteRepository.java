package org.example.pongguel.note.repository;

import org.example.pongguel.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
