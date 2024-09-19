package org.example.pongguel.book.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.domain.Book;
import org.example.pongguel.book.dto.BookDetailWithNoteListResponse;
import org.example.pongguel.book.dto.ShareBookResponse;
import org.example.pongguel.book.dto.SharedBookDetailWithNoteListResponse;
import org.example.pongguel.book.repository.BookRepository;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.jwt.JwtTokenProvider;
import org.example.pongguel.note.domain.Note;
import org.example.pongguel.note.dto.NoteDetailResponse;
import org.example.pongguel.note.repository.NoteRepository;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.service.ValidateUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShareSavedBookService {
    @Value("${share_book.share_base_url}")
    private String shareBaseUrl;

    private final BookRepository bookRepository;
    private final NoteRepository noteRepository;
    private final ValidateUser validateUser;
    private final JwtTokenProvider jwtTokenProvider;


    // 사용자의 책 공유하기, 공유 토큰 생성
    public ShareBookResponse shareBook(String token, Long bookId){
        // 1. 토큰 유효성 검사
        User user = validateUser.getUserFromToken(token);
        // 2. bookId로 책 정보 가져오기
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.BOOK_SAVED_NOT_FOUND));
        // 3. 공유 토큰 생성하기 (유효기간: 10분)
        String shareToken = jwtTokenProvider.createShareToken(user.getAccountEmail(),bookId);
        // 4. 공유 url 생성
        String shareUrl = createSharedBookUrl(shareToken);
        // 4. ShareBookResponse 생성 및 반환
        return new ShareBookResponse(
                "책이 성공적으로 공유됐습니다.",
                shareToken,
                shareUrl,
                bookId,
                book.getBookTitle(),
                user.getNickname(),
                user.getAccountEmail()
        );
    }

    // 공유한 책 정보 상세 조회 (외부 접속)
    public SharedBookDetailWithNoteListResponse getSharedBookDetails(String shareToken){
        // 1. 공유 토큰 유효성 검사
        jwtTokenProvider.validateShareToken(shareToken);
        // 2. 공유 토큰에서 bookId 추출
        Long bookId = jwtTokenProvider.getBookIdFromToken(shareToken);
        // 3. 책 정보 가져오기
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new NotFoundException(ErrorCode.BOOK_SAVED_NOT_FOUND));
        // 4. 책의 노트 리스트 가져오기
        List<Note> noteList = noteRepository.findByBook_BookIdAndIsDeletedOrderByNoteCreatedAtDesc(bookId,false);
        // 5. SharedBookDetailWithNoteListResponse 노트 리스트 생성
        List<SharedBookDetailWithNoteListResponse.NoteList> notesList = noteList.stream()
                .map(note -> new SharedBookDetailWithNoteListResponse.NoteList(
                        createNoteBySharedBookUrl(shareToken, note.getNoteId()), // 노트 공유 url
                        note.getNoteId(),
                        note.getNoteTitle(),
                        note.getNoteCreatedAt(),
                        note.isBookmarked()
                )).collect(Collectors.toList());
        // 6. BookDeatilWithNoteListResponse의 생성 및 반환
        return new SharedBookDetailWithNoteListResponse(
                "공유된 책의 상세페이지 조회의 성공했습니다.",
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

    // 공유된 책의 노트 상세 조회
    public NoteDetailResponse getSharedNoteDetail(String shareToken, Long noteId){
        // 1. 공유 토큰 유효성 검사
        jwtTokenProvider.validateShareToken(shareToken);
        // 2. 공유 토큰에서 bookId 추출
        Long bookId = jwtTokenProvider.getBookIdFromToken(shareToken);
        // 3. 책의 노트 정보 가져오기
        Note note = noteRepository.findByBook_bookIdAndNoteId(bookId,noteId)
                .orElseThrow(()->new NotFoundException(ErrorCode.NOTE_NOT_FOUND));
        // 4. NoteDetailResponse 생성 및 반환
        return new NoteDetailResponse(
                "공유된 노트 상세정보가 성공적으로 조회됐습니다.",
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

    // 공유한 책 노트 상세 조회 (외부 접속)
    // 공유한 책 상세 url 만들기
    private String createSharedBookUrl(String token){
        return String.format("%s/%s",shareBaseUrl,token);
    }
    // 공유한 노트 상세 url 만들기
    private String createNoteBySharedBookUrl(String token,Long noteId){
        return String.format("%s/%s/notes/%s",shareBaseUrl,token,noteId);
    }
}
