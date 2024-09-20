package org.example.pongguel.note.dto;

public record BookmarkNoteResponse(String message,
                                   String nickname,
                                   String bookTitle,
                                   Long noteId,
                                   String noteTitle,
                                   boolean isBookmarked) {
}
