package org.example.pongguel.note.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.example.pongguel.note.dto.NoteDeleteResponse;
import org.example.pongguel.note.dto.NoteDetailResponse;
import org.example.pongguel.note.dto.NoteWriteRequest;
import org.example.pongguel.note.service.NoteDeleteService;
import org.example.pongguel.note.service.NoteDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes-detail")
@Tag(name="Note_DETAIL", description = "노트 상세 서비스와 관련된 Api입니다.")
public class NoteDetailController {
    private final NoteDetailService noteDetailService;
    private final NoteDeleteService noteDeleteService;
    private final JwtUtil jwtUtil;

    @GetMapping("/{noteId}/read")
    @Operation(summary = "노트 상세정보를 조회합니다.", description = "작성한 노트의 상세정보를 조회합니다")
    public ResponseEntity<NoteDetailResponse> getNoteDetail(@PathVariable Long noteId,
                                                            HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        NoteDetailResponse noteDetailResponse = noteDetailService.getNoteDetail(token, noteId);
        return ResponseEntity.status(HttpStatus.OK).body(noteDetailResponse);
    }
    @PatchMapping("/{noteId}/update")
    @Operation(summary = "노트를 수정합니다.", description = "노트의 제목, 내용을 수정합니다. 수정일로 작성일이 업데이트됩니다.")
    public ResponseEntity<NoteDetailResponse> updateNoteDetail(@PathVariable Long noteId,
                                                               HttpServletRequest request,
                                                               @RequestBody NoteWriteRequest noteWriteRequest) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        NoteDetailResponse noteDetailResponse = noteDetailService.updateNoteDetail(token, noteId, noteWriteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteDetailResponse);
    }
    @PatchMapping("/{noteId}/soft-delete")
    @Operation(summary = "노트를 보관함으로 이동합니다", description = "노트를 보관함으로 이동합니다. 북마크는 자동으로 삭제됩니다. 서버에는 노트가 남아있습니다.")
    public ResponseEntity<NoteDeleteResponse> softDeleteNote(@PathVariable Long noteId,
                                                             HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        NoteDeleteResponse noteDeleteResponse = noteDeleteService.softDeleteNote(token, noteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteDeleteResponse);
    }

    @DeleteMapping("/{noteId}/final-delete")
    @Operation(summary = "노트를 완전히 삭제합니다.", description = "노트를 완전히 삭제합니다. 서버에서도 삭제됩니다.")
    public ResponseEntity<NoteDeleteResponse> finalDeleteNote(@PathVariable Long noteId,
                                                              HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        NoteDeleteResponse noteDeleteResponse = noteDeleteService.finalDeleteNote(token,noteId);
        return ResponseEntity.status(HttpStatus.OK).body(noteDeleteResponse);
    }
}
