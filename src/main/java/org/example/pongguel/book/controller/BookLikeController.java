package org.example.pongguel.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.dto.LikeBookResponse;
import org.example.pongguel.book.dto.LikedBookListResponse;
import org.example.pongguel.book.service.LikedBookService;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/liked-books")
@Tag(name="Book",description = "책 좋아요와 관련된 Api입니다.")
public class BookLikeController {
    private final LikedBookService likedBookService;
    private final JwtUtil jwtUtil;

    @PatchMapping("/{bookId}/like")
    @Operation(summary = "책 좋아요 기능입니다.", description = "사용자가 좋아요를 횔성화하거나 취소할 수 있는 기능입니다.")
    public ResponseEntity<LikeBookResponse> likeBook(HttpServletRequest request,
                                                     @PathVariable Long bookId){
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        LikeBookResponse updateLikeBookResponse = likedBookService.likeBook(token, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updateLikeBookResponse);
    }


    @GetMapping("/list")
    @Operation(summary = "사용자가 좋아요한 책을 조회합니다.", description = "사용자가 좋아요한 책 목록 리스트")
    public ResponseEntity<List<LikedBookListResponse>> getLikedBooksList(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        List<LikedBookListResponse> likedBookList = likedBookService.getLikedBooksList(token);
        return ResponseEntity.status(HttpStatus.OK).body(likedBookList);
    }

}
