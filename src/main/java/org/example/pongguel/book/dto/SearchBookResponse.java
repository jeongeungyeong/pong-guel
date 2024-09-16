package org.example.pongguel.book.dto;

public record SearchBookResponse(String bookTitle,
                                 String description,
                                 String image,
                                 String author,
                                 String publisher,
                                 Long isbn) {

}
