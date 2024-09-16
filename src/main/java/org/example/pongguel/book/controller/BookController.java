package org.example.pongguel.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.dto.SearchBookList;
import org.example.pongguel.book.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Tag(name="Book",description = "책 서비스 관련된 Api입니다.")
public class BookController {
    private final BookService bookService;

    @GetMapping("/search")
    @Operation(summary = "책 검색", description = "검색어를 입력해 네이버 책 조회 서비스를 활용합니다.")
    public ResponseEntity<SearchBookList> searchBookList(@RequestParam String query,
                                                                       @RequestParam(defaultValue = "10") int display,
                                                                       @RequestParam(defaultValue = "1") int start,
                                                                       @RequestParam(defaultValue = "sim") String sort) {
        SearchBookList response = bookService.searchBookList(query, display, start, sort);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
