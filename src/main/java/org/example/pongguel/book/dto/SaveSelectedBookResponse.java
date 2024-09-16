package org.example.pongguel.book.dto;

public record SaveSelectedBookResponse(String message,
                                       String bookTitle,
                                       String description,
                                       String imageUrl,
                                       String author,
                                       String publisher,
                                       Long isbn) {
}
