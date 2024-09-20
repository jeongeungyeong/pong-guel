package org.example.pongguel.note.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.note.domain.Note;
import org.example.pongguel.note.dto.NoteDeleteResponse;
import org.example.pongguel.note.repository.NoteRepository;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.service.ValidateUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteDeleteService {
    private final NoteRepository noteRepository;
    private final ValidateUser validateUser;

    // 노트 softdelete 삭제 (논리적 삭제)
    public NoteDeleteResponse softDeleteNote(String token, Long noteId) {
        // 1. 토큰 유효성 검사 및 사용자 권한 인증
        User user = validateUser.getUserFromToken(token);
        // 2. noteId로 노트 정보 가져오기
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()->new NotFoundException(ErrorCode.NOTE_NOT_FOUND));
        // 3. 사용자 권한 확인
        if (!note.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedException(ErrorCode.NOTE_USER_UNAUTHORIZED);
        }
        // 4. Note에서 삭제
        note.markAsDeleted();
        // 5. 북마크 상태 false로 변경
        if (note.isBookmarked() == true) {
            note.toggleBookmark();
        }
        // 6. 삭제 상황 업데이트
        Note deletedNote = noteRepository.save(note);
        // 7. NoteDeleteResponse 생성 및 반환
        return new NoteDeleteResponse(
                "노트가 보관함에 저장됐습니다.",
                deletedNote.getNoteId(),
                deletedNote.getBook().getBookTitle(),
                deletedNote.getNoteTitle(),
                deletedNote.isBookmarked(),
                deletedNote.isDeleted()
        );
    }

    // 노트 완전 삭제 (물리적 삭제)
    public NoteDeleteResponse finalDeleteNote(String token, Long noteId) {
        // 1. 토큰 유효성 검사 및 사용자 권한 인증
        User user = validateUser.getUserFromToken(token);
        // 2. noteId로 노트 정보 가져오기
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()->new NotFoundException(ErrorCode.NOTE_NOT_FOUND));
        // 3. 사용자 권한 확인
        if (!note.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedException(ErrorCode.NOTE_USER_UNAUTHORIZED);
        }
        // 4. 물리적 삭제
        noteRepository.delete(note);
        // 5. NoteDeleteResponse 생성 및 반환
        return new NoteDeleteResponse(
                "노트가 완전히 삭제됐습니다!",
                note.getNoteId(),
                note.getBook().getBookTitle(),
                note.getNoteTitle(),
                note.isBookmarked(),
                note.isDeleted()
        );
    }
}
