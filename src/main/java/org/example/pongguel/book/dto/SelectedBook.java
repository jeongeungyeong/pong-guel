package org.example.pongguel.book.dto;

public record SelectedBook(String bookTitle,
                           String description,
                           String imageUrl,
                           String author,
                           String publisher,
                           Long isbn) {
}
