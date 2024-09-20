package org.example.pongguel.note.dto;

import java.time.LocalDate;
import java.util.List;

public record NoteListResponse(
        String message,
        String nickname,
        List<NoteDetail> noteDetail
) {
    public record NoteDetail(
            Long noteId,
            Long bookId,
            String bookTitle,
            String noteTilte,
            LocalDate noteCreatedAt,
            boolean isBookmarked
    ) {}
}
