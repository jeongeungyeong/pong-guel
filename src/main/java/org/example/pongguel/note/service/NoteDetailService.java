package org.example.pongguel.note.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.note.domain.Note;
import org.example.pongguel.note.dto.NoteDetailResponse;
import org.example.pongguel.note.dto.NoteWriteRequest;
import org.example.pongguel.note.repository.NoteRepository;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.service.ValidateUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteDetailService {
    private final NoteRepository noteRepository;
    private final ValidateUser validateUser;

    // 노트 상세 조회
    public NoteDetailResponse getNoteDetail(String token, Long noteId) {
        // 1. 토큰 유효성 검사 및 사용자 권한 인증
        User user = validateUser.getUserFromToken(token);
        // 2. noteId로 노트 정보 가져오기
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()->new NotFoundException(ErrorCode.NOTE_NOT_FOUND));
        // 3. NoteDetailResponse 생성 및 반환
        return new NoteDetailResponse(
                "노트 상세정보가 성공적으로 조회됐습니다.",
                note.getUser().getNickname(),
                note.getBook().getBookId(),
                note.getBook().getBookTitle(),
                note.getBook().getImageUrl(),
                note.getNoteId(),
                note.getNoteTitle(),
                note.getContents(),
                note.getNoteCreatedAt(),
                note.isBookmarked()
        );
    }

    // 노트 수정
    public NoteDetailResponse updateNoteDetail(String token, Long noteId, NoteWriteRequest noteWriteRequest) {
        // 1. 토큰 유효성 검사 및 사용자 권한 인증
        User user = validateUser.getUserFromToken(token);
        // 2. noteId로 노트 정보 가져오기
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()->new NotFoundException(ErrorCode.NOTE_NOT_FOUND));
        // 3. Note 수정 권한 확인하기
        if (!note.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedException(ErrorCode.NOTE_USER_UNAUTHORIZED);
        }
        // 4. 노트 업데이트
        if (noteWriteRequest.noteTitle() != null) {
           note.updateNoteTitle(noteWriteRequest.noteTitle());
        }
        if (noteWriteRequest.contents() != null) {
            note.updateContents(noteWriteRequest.contents());
        }
        note.updateNoteCreatedAt(LocalDate.now());

        // 5. 업데이트 정보 노트 저장
        Note updatedNote = noteRepository.save(note);
        return new NoteDetailResponse(
                "노트가 성공적으로 수정됐습니다.",
                updatedNote.getUser().getNickname(),
                updatedNote.getBook().getBookId(),
                updatedNote.getBook().getBookTitle(),
                updatedNote.getBook().getImageUrl(),
                updatedNote.getNoteId(),
                updatedNote.getNoteTitle(),
                updatedNote.getContents(),
                updatedNote.getNoteCreatedAt(),
                updatedNote.isBookmarked()
        );
    }
}
