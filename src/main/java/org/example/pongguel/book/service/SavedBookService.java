package org.example.pongguel.book.service;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.book.domain.Book;
import org.example.pongguel.book.dto.SavedBookListResponse;
import org.example.pongguel.book.repository.BookRepository;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.service.ValidateUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SavedBookService {
    private final BookRepository bookRepository;
    private final ValidateUser validateUser;

    // 사용자가 저장한 책 조회
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
    // 책 상세보기

    // 사용자가 저장한 책 삭제
}
