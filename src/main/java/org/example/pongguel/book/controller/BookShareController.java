package org.example.pongguel.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.dto.SharedBookDetailWithNoteListResponse;
import org.example.pongguel.book.service.ShareSavedBookService;
import org.example.pongguel.note.dto.NoteDetailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shared")
@RequiredArgsConstructor
@Tag(name="SharedBook",description = "외부인의 공유된 책 관련 서비스 관련된 Api입니다.")
public class BookShareController {
    private final ShareSavedBookService shareSavedBookService;

    // 외부인 책 상세 열람
    @GetMapping("/books/{shareToken}")
    @Operation(summary = "외부인이 공유된 링크로 10분간 책 정보 상세보기", description = "외부인은 퐁글 이용자에게 받은 url로 접속합니다. " +
            " 책 정보와 사용자가 작성한 노트 목록을 볼 수 있습니다.")
    public ResponseEntity<SharedBookDetailWithNoteListResponse> getSharedBookDetails(@PathVariable String shareToken) {
        SharedBookDetailWithNoteListResponse SharedBookDetailWithNoteListResponse = shareSavedBookService.getSharedBookDetails(shareToken);
        return ResponseEntity.status(HttpStatus.OK).body(SharedBookDetailWithNoteListResponse);
    }
    // 외부인 노트 상세 열람
    @GetMapping("/books/{shareToken}/notes/{noteId}")
    @Operation(summary = "외부인이 공유된 링크로 10분간 책의 모든 노트 접근하기", description = "외부인은 공유된 책의 노트를 열람할 수 있습니다.")
    public ResponseEntity<NoteDetailResponse> getNotesBySharedBook(@PathVariable("shareToken") String shareToken,
                                                                   @PathVariable("noteId") Long noteId) {
        NoteDetailResponse noteDetailResponse = shareSavedBookService.getSharedNoteDetail(shareToken,noteId);
        return ResponseEntity.status(HttpStatus.OK).body(noteDetailResponse);
    }
}
