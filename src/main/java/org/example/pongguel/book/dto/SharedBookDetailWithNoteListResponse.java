package org.example.pongguel.book.dto;

import java.time.LocalDate;
import java.util.List;

public record SharedBookDetailWithNoteListResponse(
        String message,
        Long bookId,
        String bookTitle,
        String description,
        String imageUrl,
        String author,
        Long isbn,
        String publisher,
        boolean isLiked,
        List<NoteList> noteList
){
         public record NoteList(
            String noteShareUrl,
            Long noteId,
            String noteTitle,
            LocalDate createdAt,
            boolean isBookmarked
    ){}
}
