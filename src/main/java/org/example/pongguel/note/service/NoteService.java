package org.example.pongguel.note.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.domain.Book;
import org.example.pongguel.book.repository.BookRepository;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.note.domain.Note;
import org.example.pongguel.note.dto.NoteListResponse;
import org.example.pongguel.note.dto.NoteWriteRequest;
import org.example.pongguel.note.dto.NoteDetailResponse;
import org.example.pongguel.note.repository.NoteRepository;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.service.ValidateUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {
    private final NoteRepository noteRepository;
    private final BookRepository bookRepository;
    private final ValidateUser validateUser;

    // 노트 생성
    public NoteDetailResponse createNote(String token, Long bookId, NoteWriteRequest noteWriteRequest) {
        // 1. 토큰 유효성 검사 및 사용자 권한 인증
        User user = validateUser.getUserFromToken(token);
        // 2. bookId로 책 정보 및 사용자의 저장 여부 확인
        Book book = bookRepository.findByBookIdAndUser_UserId(bookId,user.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_SAVED_NOT_FOUND));
        // 3. Note 생성
        Note note = Note.builder()
                .noteTitle(noteWriteRequest.noteTitle())
                .contents(noteWriteRequest.contents())
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

    // 휴지통으로 이동한 노트 목록 조회
    public List<NoteListResponse> getDeleteNotes(String token) {
        return getNotesByDeletedStatus(token, true);
    }
    // 생성된 사용자 노트 목록 조회
    public List<NoteListResponse> getActiveNotes(String token) {
        return getNotesByDeletedStatus(token, false);
    }

    /*
     * 사용자 노트 전체 조회
     * 1. isDeleted가 true인 경우 - 활성화
     * 2. isDeleted가 false인 경우 - 휴지통(보관함)
     * */
    private List<NoteListResponse> getNotesByDeletedStatus(String token, boolean isDeleted) {
        // 1. 토큰 유효성 검사 및 사용자 권한 인증
        User user = validateUser.getUserFromToken(token);
        // 2. 사용자가 작성한 노트 조회
        List<Note> noteList = noteRepository.findAllByUser_userIdAndIsDeletedOrderByNoteCreatedAtDesc(user.getUserId(), isDeleted);
        // 3. 노트가 없는 경우 예외 처리
        if (noteList.isEmpty()) {
                throw new NotFoundException(ErrorCode.NOTE_NOT_FOUND);
        }
        // 4. 노트 정보를 NoteDetailResponse 객체로 변환
        List<NoteListResponse.NoteDetail> noteDetails = noteList.stream()
                .map(note -> new NoteListResponse.NoteDetail(
                        note.getNoteId(),
                        note.getBook().getBookId(),
                        note.getBook().getBookTitle(),
                        note.getNoteTitle(),
                        note.getNoteCreatedAt(),
                        note.isBookmarked()
                )).collect(Collectors.toList());
        // isDeleted = true (휴지통), false (활성화)
        String message = isDeleted ? "휴지통(보관함) 노트 목록입니다." : "활성화된 노트 목록입니다.";

        // 5.
        return Collections.singletonList(new NoteListResponse(
                message,
                user.getNickname(),
                noteDetails
        ));
    }

    // NoteCreateResponse 생성 및 반환
    private NoteDetailResponse createNoteCreateResponse(Note note) {
        return new NoteDetailResponse(
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
