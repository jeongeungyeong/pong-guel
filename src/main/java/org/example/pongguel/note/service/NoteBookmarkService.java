package org.example.pongguel.note.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.dto.LikedBookListResponse;
import org.example.pongguel.exception.ConflictException;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.note.domain.Note;
import org.example.pongguel.note.dto.BookmarkNoteResponse;
import org.example.pongguel.note.dto.BookmarkedNoteListResponse;
import org.example.pongguel.note.repository.NoteRepository;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.service.ValidateUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteBookmarkService {
    private final NoteRepository noteRepository;
    private final ValidateUser validateUser;

    // 즐겨찾기한 노트 조회
    public List<BookmarkedNoteListResponse> getBookmarkedNotesList(String token) {
        // 1. 토큰 유효성 검사 및 사용자 정보 반환
        User user = validateUser.getUserFromToken(token);
        // 2. 사용자 노트 북마크 정보 조회
        List<Note> bookmarkedNotes = noteRepository.findByUser_UserIdAndIsBookmarked(user.getUserId());
        // 3. 북마크 한 노트 정보 조회
        return bookmarkedNotes.stream()
                .map(note -> new BookmarkedNoteListResponse(
                        "북마크 한 노트 정보입니다.",
                        note.getUser().getNickname(),
                        note.getBook().getBookId(),
                        note.getBook().getBookTitle(),
                        note.getNoteId(),
                        note.getNoteTitle(),
                        note.isBookmarked()
                )).collect(Collectors.toList());
    }

    // 노트 즐겨찾기 기능
    public BookmarkNoteResponse bookmarkNote(String token, Long noteId) {
        // 1. 토큰 유효성 검사 및 사용자 권한 인증
        User user = validateUser.getUserFromToken(token);
        // 2. noteId로 노트 정보 가져오기
        Note note = noteRepository.findById(noteId)
                .orElseThrow(()->new NotFoundException(ErrorCode.NOTE_NOT_FOUND));
        // 3. 사용자 권한 확인
        if (!note.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedException(ErrorCode.NOTE_USER_UNAUTHORIZED);
        }
        if (note.isDeleted() == true){
            throw new ConflictException(ErrorCode.NOTE_BOOKMARK_CONFLICT);
        }
        // 4. 북마크 상태 변경
        note.toggleBookmark();
        // 5. 노트 상태 변경 저장
        Note newdNote = noteRepository.save(note);

        return createBookmarkNoteResponse(newdNote);
    }

    // 북마크 상태 변경 메서드
    private BookmarkNoteResponse createBookmarkNoteResponse(Note newNote){
        return new BookmarkNoteResponse(
                "노트 북마크 상태가 변경됐습니다.",
                newNote.getUser().getNickname(),
                newNote.getBook().getBookTitle(),
                newNote.getNoteId(),
                newNote.getNoteTitle(),
                newNote.isBookmarked()
        );
    }
}
