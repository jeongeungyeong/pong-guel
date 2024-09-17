package org.example.pongguel.note.repository;

import org.example.pongguel.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // 북마크 노트 좋아요 정보 조회
    @Query("SELECT n FROM Note n WHERE n.user.userId = :userId AND n.isBookmarked=true")
    List<Note> findByUser_UserIdAndIsBookmarked(@Param("userId") UUID userId);
}
