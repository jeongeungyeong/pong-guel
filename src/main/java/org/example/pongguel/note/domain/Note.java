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

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    // 제목 업데이트
    public void updateNoteTitle(String newTitle){
        if (newTitle != null && !newTitle.trim().isEmpty()){
            this.noteTitle = newTitle;
        }
    }

    // 내용 업데이트
    public void updateContents(String newContents) {
        if (newContents != null) {
            this.contents = newContents;
        }
    }

    // 생성일 -> 수정일로 업데이트
    public void updateNoteCreatedAt(LocalDate newDate) {
        if (newDate != null) {
            this.noteCreatedAt = newDate;
        }
    }

    // 즐겨찾기 기능
    public void toggleBookmark(){
        this.isBookmarked=!this.isBookmarked;
    }

    // softDelete 기능
    public void markAsDeleted(){
        this.isDeleted= true;
    }
}
