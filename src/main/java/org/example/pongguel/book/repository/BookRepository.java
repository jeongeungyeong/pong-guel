package org.example.pongguel.book.repository;

import org.example.pongguel.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    //ISBN으로 책 정보 찾기
    Optional<Book> findByIsbn(Long isbn);
}
