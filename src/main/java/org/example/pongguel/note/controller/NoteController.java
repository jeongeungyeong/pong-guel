package org.example.pongguel.note.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.example.pongguel.note.dto.NoteCreateRequest;
import org.example.pongguel.note.dto.NoteCreateResponse;
import org.example.pongguel.note.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
@Tag(name="Note", description = "노트 서비스와 관련된 Api입니다.")
public class NoteController {
    private final NoteService noteService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{bookId}/write")
    @Operation(summary = "노트를 저장합니다.", description = "사용자가 책에 관하여 작성한 노트를 저장합니다.")
    public ResponseEntity<NoteCreateResponse> createNote(HttpServletRequest request,
                                                         @PathVariable Long bookId,
                                                         @RequestBody NoteCreateRequest noteCreateRequest) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        NoteCreateResponse noteCreateResponse = noteService.createNote(token,bookId,noteCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteCreateResponse);
    }
}
