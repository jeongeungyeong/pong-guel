package org.example.pongguel.note.dto;

import java.time.LocalDate;

public record NoteDetailResponse(String message,
                                 String nickname,
                                 Long bookId,
                                 String bookTitle,
                                 String imageUrl,
                                 Long noteId,
                                 String noteTitle,
                                 String contents,
                                 LocalDate noteCreatedAt,
                                 boolean isBookmarked) {
}
