package org.example.pongguel.note.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.domain.Book;
import org.example.pongguel.book.repository.BookRepository;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.note.domain.Note;
import org.example.pongguel.note.dto.NoteCreateRequest;
import org.example.pongguel.note.dto.NoteCreateResponse;
import org.example.pongguel.note.repository.NoteRepository;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.service.ValidateUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {
    private final NoteRepository noteRepository;
    private final BookRepository bookRepository;
    private final ValidateUser validateUser;

    // 노트 생성
    public NoteCreateResponse createNote(String token, Long bookId, NoteCreateRequest noteCreateRequest) {
        // 1. 토큰 유효성 검사 및 사용자 권한 인증
        User user = validateUser.getUserFromToken(token);
        // 2. bookId로 책 정보 및 사용자의 저장 여부 확인
        Book book = bookRepository.findByBookIdAndUser_UserId(bookId,user.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_SAVED_NOT_FOUND));
        // 3. Note 생성
        Note note = Note.builder()
                .noteTitle(noteCreateRequest.noteTitle())
                .contents(noteCreateRequest.contents())
                .noteCreatedAt(LocalDate.now())
                .isBookmarked(false)
                .user(user)
                .book(book)
                .build();
        // 4. 노트 저장
        noteRepository.save(note);
        // 5. NoteCreateResponse 생성 및 반환
        return createNoteCreateResponse(note);
    }
    // 노트 상세 조회
    // 노트 수정
    // 노트 삭제
    // 노트 전체 조회
    // 노트 즐겨찾기
    // 즐겨찾기한 노트 조회

    // NoteCreateResponse 생성 및 반환
    private NoteCreateResponse createNoteCreateResponse(Note note) {
        return new NoteCreateResponse(
                "노트가 성공적으로 저장됐습니다.",
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
}
