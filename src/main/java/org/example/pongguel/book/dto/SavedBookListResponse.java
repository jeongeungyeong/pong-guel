package org.example.pongguel.book.dto;

public record SavedBookListResponse(String message,
                                    Long bookId,
                                    String bookTitle,
                                    String imageUrl,
                                    String author,
                                    boolean isLiked) {
}
