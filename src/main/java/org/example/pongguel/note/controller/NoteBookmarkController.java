package org.example.pongguel.note.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.example.pongguel.note.dto.BookmarkNoteResponse;
import org.example.pongguel.note.dto.BookmarkedNoteListResponse;
import org.example.pongguel.note.service.NoteBookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarked-notes")
@Tag(name="Note_BOOKMARK", description = "노트 즐겨찾기 서비스와 관련된 Api입니다.")
public class NoteBookmarkController {
    private final NoteBookmarkService noteBookmarkService;
    private final JwtUtil jwtUtil;

    @PatchMapping("/{noteId}/bookmark")
    @Operation(summary = "노트 북마크 기능입니다.", description = "노트 북마크 활성화, 취소 기능입니다.")
    public ResponseEntity<BookmarkNoteResponse> bookmarkNote(HttpServletRequest request,
                                                             @PathVariable Long noteId){
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        BookmarkNoteResponse bookmarkNoteResponse = noteBookmarkService.bookmarkNote(token, noteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookmarkNoteResponse);
    }

    @GetMapping("/list")
    @Operation(summary = "북마크한 노트를 조회합니다.", description = "사용자가 북마크한 모든 노트를 조회합니다.")
    public ResponseEntity<List<BookmarkedNoteListResponse>> getAllBookmarkedNotes(HttpServletRequest request){
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        List<BookmarkedNoteListResponse> bookmarkedNoteList = noteBookmarkService.getBookmarkedNotesList(token);
        return ResponseEntity.status(HttpStatus.OK).body(bookmarkedNoteList);
    }
}

