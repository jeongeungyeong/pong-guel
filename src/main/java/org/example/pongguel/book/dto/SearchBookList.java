package org.example.pongguel.book.dto;

import java.util.List;

public record SearchBookList(String message,
                             Long total,
                             List<SearchBookResponse> searchBookResponse) {
}