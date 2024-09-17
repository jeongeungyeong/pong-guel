package org.example.pongguel.book.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.domain.Book;
import org.example.pongguel.book.dto.LikeBookResponse;
import org.example.pongguel.book.dto.LikedBookListResponse;
import org.example.pongguel.book.repository.BookRepository;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.service.ValidateUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LikedBookService {
    private final BookRepository bookRepository;
    private final ValidateUser validateUser;

    // 책 좋아요 기능
    public LikeBookResponse likeBook(String token, Long bookId){
        // 1. 토큰 유효성 검사 및 사용자 정보 반환
        User user = validateUser.getUserFromToken(token);
        // 2. 저장한 책에서 정보 가져오기
        Book book = bookRepository.findByBookIdAndUser_UserId(bookId,user.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_SAVED_NOT_FOUND));
        // 3. 좋아요 상태 변경 기능
        book.toggleLike();
        // 4. 좋아요 상태 변경 저장
        Book newbook = bookRepository.save(book);

        return createLikeBookResponse(newbook);
    }

    // 사용자의 책 좋아요 리스트 조회
    // isLiked = true 불러오기
    public List<LikedBookListResponse> getLikedBooksList(String token){
        // 1. 토큰 유효성 검사 및 사용자 정보 반환
        User user = validateUser.getUserFromToken(token);
        // 2. 사용자 책 좋아요 정보 조회
        List<Book> likedBooks = bookRepository.findByUser_UserIdAndIsLiked(user.getUserId());
        // 3. 좋아요 한 책 리스트를 응답으로 변환
        return likedBooks.stream()
                .map(book-> new LikedBookListResponse(
                        "좋아요 한 책 정보입니다.",
                        book.getBookId(),
                        book.getBookTitle(),
                        book.getImageUrl(),
                        book.getAuthor(),
                        book.isLiked()
                )).collect(Collectors.toList());
    }

    // LikeBookResponse 생성
    private LikeBookResponse createLikeBookResponse(Book newBook){
        return new LikeBookResponse(
                "책 좋아요 상태가 변경됐습니다.",
                newBook.getBookTitle(),
                newBook.getImageUrl(),
                newBook.getAuthor(),
                newBook.isLiked()
        );
    }
}
