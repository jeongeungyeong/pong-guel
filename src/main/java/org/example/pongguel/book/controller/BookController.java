package org.example.pongguel.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.dto.SaveSelectedBookResponse;
import org.example.pongguel.book.dto.SearchBookList;
import org.example.pongguel.book.service.BookService;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Tag(name="Book",description = "책 서비스 관련된 Api입니다.")
public class BookController {
    private final BookService bookService;
    private final JwtUtil jwtUtil;

    @GetMapping("/search")
    @Operation(summary = "책 검색", description = "검색어를 입력해 네이버 책 조회 서비스를 활용합니다.")
    public ResponseEntity<SearchBookList> searchBookList(@RequestParam String query,
                                                                       @RequestParam(defaultValue = "10") int display,
                                                                       @RequestParam(defaultValue = "1") int start,
                                                                       @RequestParam(defaultValue = "sim") String sort) {
        SearchBookList response = bookService.searchBookList(query, display, start, sort);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/save")
    @Operation(summary = "사용자가 검색한 책을 저장합니다.", description = "책의 ISBN을 활용해 상세 정보를 조회하고 저장합니다.")
    public ResponseEntity<SaveSelectedBookResponse> saveSelectedBook(HttpServletRequest request,
                                                                     @RequestParam Long isbn){
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        SaveSelectedBookResponse saveSelectedBook = bookService.SaveSelectedBook(token,isbn);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveSelectedBook);
    }

}
