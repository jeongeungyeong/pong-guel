package org.example.pongguel.note.dto;

public record BookmarkedNoteListResponse(String message,
                                         String nickname,
                                         Long bookId,
                                         String bookTitle,
                                         Long noteId,
                                         String noteTitle,
                                         boolean isBookmarked) {
}
