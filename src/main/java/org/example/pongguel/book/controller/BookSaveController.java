package org.example.pongguel.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.dto.SavedBookListResponse;
import org.example.pongguel.book.service.SavedBookService;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saved-books")
@Tag(name="Book",description = "책 서비스 관련된 Api입니다.")
public class BookSaveController {
    private final SavedBookService savedBookService;
    private final JwtUtil jwtUtil;

    // refactor: 좋아요 여부도 표시하기
    // 노트 목록까지 보기
    @GetMapping("/list")
    @Operation(summary = "사용자가 저장한 책을 조회합니다.", description = "사용자가 저장한 모든 책을 조회합니다.")
    public ResponseEntity<List<SavedBookListResponse>> getSavedBookList(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        List<SavedBookListResponse> savedUserBookList = savedBookService.findSavedBookList(token);
        return ResponseEntity.status(HttpStatus.OK).body(savedUserBookList);
    }

    // 저장한 책 상세보기
    // 저장한 책 삭제하기
}
