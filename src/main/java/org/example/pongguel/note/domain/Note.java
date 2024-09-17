package org.example.pongguel.note.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.pongguel.book.domain.Book;
import org.example.pongguel.user.domain.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long noteId;

    @Column(nullable = false)
    @Size(min = 1, max = 50)
    private String noteTitle;

    @Column(nullable = false)
    @Size(min = 1, max = 200)
    private String contents;

    @CreationTimestamp
    private LocalDate noteCreatedAt;

    @Column(name = "is_bookmarked",nullable = false)
    private boolean isBookmarked=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    // 즐겨찾기 기능
    public void toggleBookmark(){
        this.isBookmarked=!this.isBookmarked;
    }
}
