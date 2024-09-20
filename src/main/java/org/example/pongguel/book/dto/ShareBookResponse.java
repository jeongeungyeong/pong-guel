package org.example.pongguel.book.dto;

public record ShareBookResponse(String message,
                                String shareToken,
                                String shareUrl,
                                Long bookId,
                                String bookTitle,
                                String nickname,
                                String userEmail) {
}
