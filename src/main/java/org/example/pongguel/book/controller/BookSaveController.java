package org.example.pongguel.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.dto.BookDetailWithNoteListResponse;
import org.example.pongguel.book.dto.DeleteSavedBookResponse;
import org.example.pongguel.book.dto.SavedBookListResponse;
import org.example.pongguel.book.dto.ShareBookResponse;
import org.example.pongguel.book.service.SavedBookService;
import org.example.pongguel.book.service.ShareSavedBookService;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saved-books")
@Tag(name="Book_SAVE",description = "사용자가 저장한 책 서비스 관련된 Api입니다.")
public class BookSaveController {
    private final SavedBookService savedBookService;
    private final ShareSavedBookService shareSavedBookService;
    private final JwtUtil jwtUtil;

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
    @GetMapping("/{bookId}/detail")
    @Operation(summary = "사용자가 저장한 책의 상세정보를 조회합니다.", description = "사용자가 저장한 책의 정보와 좋아요, 노트리스트를 조회합니다.")
    public ResponseEntity<BookDetailWithNoteListResponse> getBookDetails(HttpServletRequest request,
                                                                         @PathVariable Long bookId){
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        BookDetailWithNoteListResponse bookDetails = savedBookService.getBookDetails(token,bookId);
        return ResponseEntity.status(HttpStatus.OK).body(bookDetails);
    }
    @DeleteMapping("/{bookId}/deleted")
    @Operation(summary = "사용자가 저장한 책을 삭제합니다.", description = "사용자가 저장한 책을 삭제합니다. 함께 작성된 노트도 모두 삭제됩니다.")
    public ResponseEntity<DeleteSavedBookResponse> DeleteSavedBook(HttpServletRequest request,
                                                                  @PathVariable Long bookId){
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        DeleteSavedBookResponse deleteSavedBookResponse = savedBookService.DeleteSavedBook(token,bookId);
        return ResponseEntity.status(HttpStatus.OK).body(deleteSavedBookResponse);
    }
    @PostMapping("/{bookId}/share")
    @Operation(summary = "사용자가 저장한 책을 외부로 공유합니다.", description = "사용자가 저장한 책을 외부로 공유합니다. 공유토큰도 함께 생성됩니다.")
    public ResponseEntity<ShareBookResponse> shareBook(HttpServletRequest request,
                                                       @PathVariable Long bookId){
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        ShareBookResponse shareBookResponse = shareSavedBookService.shareBook(token,bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(shareBookResponse);
    }
}
