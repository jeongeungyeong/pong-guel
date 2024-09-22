package org.example.pongguel.book.repository;

import org.example.pongguel.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, Long> {
    //ISBN으로 책 정보 찾기
    Optional<Book> findByIsbn(Long isbn);
    //UserID를  사용해서 책 정보 찾기
    List<Book> findByUser_UserId(UUID userId);
    // 사용자가 저장한 책 정보 찾기
    Optional<Book> findByBookIdAndUser_UserId(Long bookId, UUID userId);
   // 사용자가 좋아요한 책 정보 찾기
    @Query("SELECT b FROM Book b WHERE b.user.userId = :userId AND b.isLiked=true")
    List<Book> findByUser_UserIdAndIsLiked(@Param("userId") UUID userId);
    // 사용자가 저장한 책 삭제
    void deleteAllBooksByUser_UserId(UUID userId);
}
