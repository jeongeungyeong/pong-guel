package org.example.pongguel.book.dto;

public record SearchBookRequest(String query,
                                Long display,
                                Long start,
                                String sort) {
}
