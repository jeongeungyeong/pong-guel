package org.example.pongguel.book.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.pongguel.user.domain.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long bookID;

    @Column(nullable = false)
    private String bookTitle;

    private String description;
    private String imageUrl;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false, unique = true)
    private Long isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
