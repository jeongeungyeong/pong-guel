package org.example.pongguel.note.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.example.pongguel.note.dto.NoteListResponse;
import org.example.pongguel.note.dto.NoteWriteRequest;
import org.example.pongguel.note.dto.NoteDetailResponse;
import org.example.pongguel.note.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
@Tag(name="Note", description = "노트 서비스와 관련된 Api입니다.")
public class NoteController {
    private final NoteService noteService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{bookId}/write")
    @Operation(summary = "노트를 저장합니다.", description = "사용자가 책에 관하여 작성한 노트를 저장합니다.")
    public ResponseEntity<NoteDetailResponse> createNote(HttpServletRequest request,
                                                         @PathVariable Long bookId,
                                                         @RequestBody NoteWriteRequest noteWriteRequest) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        NoteDetailResponse noteDetailResponse = noteService.createNote(token,bookId, noteWriteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteDetailResponse);
    }

    @GetMapping("/active-list")
    @Operation(summary = "활성화 노트목록을 조회합니다.", description = "사용자가 작성한 노트 목록을 조회합니다.")
    public ResponseEntity<List<NoteListResponse>> getActiveNotes(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        List<NoteListResponse> noteListResponses = noteService.getActiveNotes(token);
        return ResponseEntity.status(HttpStatus.OK).body(noteListResponses);
    }

    @GetMapping("/deleted-list")
    @Operation(summary = "휴지통의 노트목록을 조회합니다.", description = "사용자가 삭제한 노트 목록을 조회합니다.")
    public ResponseEntity<List<NoteListResponse>> getDeleteNotes(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        List<NoteListResponse> noteListResponses = noteService.getDeleteNotes(token);
        return ResponseEntity.status(HttpStatus.OK).body(noteListResponses);
    }
}
