package org.example.pongguel.note.repository;

import org.example.pongguel.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // 북마크 노트 좋아요 정보 조회
    @Query("SELECT n FROM Note n WHERE n.user.userId = :userId AND n.isBookmarked=true")
    List<Note> findByUser_UserIdAndIsBookmarked(@Param("userId") UUID userId);
    // 책의 노트 리스트
    List<Note> findByBook_BookIdAndIsDeletedOrderByNoteCreatedAtDesc(Long bookId, boolean isDeleted);
    // 사용자가 작성한 노트 조회 (db에서 노트 최신순으로 정렬)
    List<Note> findAllByUser_userIdAndIsDeletedOrderByNoteCreatedAtDesc(UUID userId, boolean isDeleted);
    // bookId와 noteId로 조회
    Optional<Note> findByBook_bookIdAndNoteId(Long bookId, Long noteId);
}
