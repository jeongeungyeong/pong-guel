package org.example.pongguel.book.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.domain.Book;
import org.example.pongguel.book.dto.BookDetailWithNoteListResponse;
import org.example.pongguel.book.dto.DeleteSavedBookResponse;
import org.example.pongguel.book.dto.SavedBookListResponse;
import org.example.pongguel.book.repository.BookRepository;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.note.domain.Note;
import org.example.pongguel.note.repository.NoteRepository;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.service.ValidateUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SavedBookService {
    private final BookRepository bookRepository;
    private final NoteRepository noteRepository;
    private final ValidateUser validateUser;

    // 사용자가 저장한 책 전체 조회
    public List<SavedBookListResponse> findSavedBookList(String token){
        // 1. 토큰 유효성 검사 및 사용자 정보 반환
        User user = validateUser.getUserFromToken(token);
        // 2. 사용자 저장 책 정보 조회
        List<Book> savedBookList = bookRepository.findByUser_UserId(user.getUserId());
        // 3. 결과 변환 및 반환
        if (savedBookList.isEmpty()) {
            // 정보가 없는 경우
            return List.of(new SavedBookListResponse("저장된 책 정보가 없습니다.",0L ,"", "", "",false));
        } else {
            // 정보가 있는 경우
            return savedBookList.stream()
                    .map(book -> new SavedBookListResponse(
                            "저장하신 책 정보입니다.",
                            book.getBookId(),
                            book.getBookTitle(),
                            book.getImageUrl(),
                            book.getAuthor(),
                            book.isLiked()
                    ))
                    .collect(Collectors.toList());
        }
    }
    // 책 상세 보기
    public BookDetailWithNoteListResponse getBookDetails(String token, Long bookId){
        // 1. 토큰 유효성 검사
        User user = validateUser.getUserFromToken(token);
        // 2. bookId로 책 정보 가져오기
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.BOOK_SAVED_NOT_FOUND));
        // 3. 책의 노트 리스트 가져오기
        List<Note> noteList = noteRepository.findByBook_BookIdAndIsDeletedOrderByNoteCreatedAtDesc(bookId,false);
        // 4. BookDeatilWithNoteListResponse의 노트 리스트 생성
        List<BookDetailWithNoteListResponse.NoteList> notesList = noteList.stream()
                .map(note -> new BookDetailWithNoteListResponse.NoteList(
                        note.getNoteId(),
                        note.getNoteTitle(),
                        note.getNoteCreatedAt(),
                        note.isBookmarked()
                )).collect(Collectors.toList());
        // 5. BookDeatilWithNoteListResponse의 생성 및 반환
        return new BookDetailWithNoteListResponse(
                "책의 상세페이지 조회의 성공했습니다.",
                book.getBookId(),
                book.getBookTitle(),
                book.getDescription(),
                book.getImageUrl(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublisher(),
                book.isLiked(),
                notesList
        );
    }
    // 사용자가 저장한 책 삭제
    public DeleteSavedBookResponse DeleteSavedBook(String token, Long bookId){
        // 1. 토큰 유효성 검사
        User user = validateUser.getUserFromToken(token);
        // 2. bookId로 책 정보 가져오기
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.BOOK_SAVED_NOT_FOUND));
        // 3. 책의 모든 노트 리스트 가져오기 및 노트 개수
        List<Note> noteList = noteRepository.findByBook_BookId(bookId);
        // 4. 사용자 권한 확인
        if (!book.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedException(ErrorCode.BOOK_DELETED_USER_UNAUTHORIZED);
        }
        // 5. DeleteSavedBookResponse의 노트 리스트 생성 및 노트 숫자 세기
        List<DeleteSavedBookResponse.NoteList> notesList = new ArrayList<>();
        long noteCount = 0;
        for (Note note : noteList) {
            notesList.add(new DeleteSavedBookResponse.NoteList(note.getNoteId(), note.getNoteTitle()));
            noteCount++;
        }
        // 6. 노트 전체 삭제
        noteRepository.deleteByBook_BookId(bookId);
        // 7. 책 삭제
        bookRepository.delete(book);
        // 7. DeleteSavedBookResponse의 생성 및 반환
        return new DeleteSavedBookResponse(
                "책이 성공적으로 저장목록에서 삭제됐습니다.",
                book.getBookId(),
                book.getBookTitle(),
                noteCount,
                notesList
        );
    }


}
