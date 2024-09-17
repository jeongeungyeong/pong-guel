package org.example.pongguel.book.dto;

public record LikedBookListResponse(String message,
                                    Long bookId,
                                    String bookTitle,
                                    String imageUrl,
                                    String author,
                                    boolean isLiked) {
}
