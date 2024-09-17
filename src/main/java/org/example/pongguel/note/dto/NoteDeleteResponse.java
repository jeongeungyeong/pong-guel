package org.example.pongguel.note.dto;

public record NoteDeleteResponse(String message,
                                 Long noteId,
                                 String bookTitle,
                                 String noteTitle,
                                 boolean isBookmarked,
                                 boolean isDeleted) {
}
