package org.example.pongguel.book.dto;

import java.util.List;

public record DeleteSavedBookResponse(
        String message,
        Long bookId,
        String bookTitle,
        Long noteCount,
        List<NoteList> noteList
) {
    public record NoteList(
            Long noteId,
            String noteTitle
    ){}
}
