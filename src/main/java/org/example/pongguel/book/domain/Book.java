package org.example.pongguel.book.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.pongguel.user.domain.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private String bookTitle;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private String imageUrl;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false, unique = true)
    private Long isbn;

    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "is_liked", nullable = false)
    private boolean isLiked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 좋아요 기능
    public void toggleLike(){
        this.isLiked = !this.isLiked;
    }
}
