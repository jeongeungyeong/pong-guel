package org.example.pongguel.book.dto;

public record LikeBookResponse(String message,
                               String bookTitle,
                               String imageUrl,
                               String author,
                               boolean isLiked) {
}
