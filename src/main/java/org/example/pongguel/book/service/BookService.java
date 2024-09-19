package org.example.pongguel.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pongguel.book.domain.Book;
import org.example.pongguel.book.dto.*;
import org.example.pongguel.book.repository.BookRepository;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.NotFoundException;
import org.example.pongguel.note.domain.Note;
import org.example.pongguel.note.repository.NoteRepository;
import org.example.pongguel.user.domain.User;
import org.example.pongguel.user.service.ValidateUser;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {
    @Value("${naver.client_id}")
    private String clientId;
    @Value("${naver.client_secret}")
    private String clientSecret;
    @Value("${naver.book_base_url}")
    private String bookBasehUrl;

    private final WebClient webClient;
    private final BookRepository bookRepository;
    private final NoteRepository noteRepository;
    private final ValidateUser validateUser;

    // 네이버 책 조회하기
    public SearchBookList searchBookList (String query, int display, int start, String sort) {
        // 1. 검색어는 UTF-8로 인코딩하기
        String encodedQuery = UriUtils.encode(query, "UTF-8");

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(bookBasehUrl)
                        .path("/book.json")
                        .queryParam("query", encodedQuery)
                        .queryParam("display", display)
                        .queryParam("start", start)
                        .queryParam("sort", sort)
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> {
                    List<SearchBookResponse> bookList = new ArrayList<>();
                    JsonNode items = jsonNode.get("items");
                    Long total = jsonNode.get("total").asLong();
                    for (JsonNode item : items) {
                        SearchBookResponse book = new SearchBookResponse(
                                item.path("title").asText(),
                                item.path("description").asText(),
                                item.path("image").asText(),
                                item.path("author").asText(),
                                item.path("publisher").asText(),
                                item.path("isbn").asLong()
                        );
                        bookList.add(book);
                    }
                    return new SearchBookList("책 조회에 성공했습니다!",total,bookList);
                })
                .block();
    }

    // 사용자의 책 선택 및 저장
    public SaveSelectedBookResponse SaveSelectedBook (String token,Long isbn){
        // 1. 토큰 유효성 검사
        User user = validateUser.getUserFromToken(token);
        // 2. 검색 결과에서 찾은 ISBN으로 책 찾기
        SelectedBook selectedBook = findBookByIsbn(isbn);
        // 3.  책 저장 또는 조회 Book 객체 생성
        Book book = saveOrGetBook(selectedBook,user);
        // 4. SaveSelectedBookResponse 생성 및 반환
        return createSaveSelectedBookResponse(book);
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

    // ISBN으로 책 조회
    private SelectedBook findBookByIsbn(Long isbn){
       return webClient.get()
               .uri(uriBuilder -> uriBuilder
                       .scheme("https")
                       .host(bookBasehUrl)
                       .path("/book_adv.xml")
                       .queryParam("d_isbn",isbn)
                       .build())
               .header("X-Naver-Client-Id", clientId)
               .header("X-Naver-Client-Secret", clientSecret)
               .retrieve()
               .bodyToMono(String.class)
               .map(xmlString -> {
                           JSONObject jsonObject = XML.toJSONObject(xmlString); // xml -> json으로 변환
                           JSONObject itemObject = jsonObject.getJSONObject("rss").getJSONObject("channel").getJSONObject("item");
                           return new SelectedBook(
                                   itemObject.getString("title"),
                                   itemObject.getString("description"),
                                   itemObject.getString("image"),
                                   itemObject.getString("author"),
                                   itemObject.getString("publisher"),
                                   itemObject.getLong("isbn")
                           );
                       })
               .block();
    }
    // 책 저장 또는 조회
    private Book saveOrGetBook(SelectedBook book,User user){
        return bookRepository.findByIsbn(book.isbn())
                .orElseGet(()->{
                    Book newBook = createBookFromSelectBook(book,user);
                    return bookRepository.save(newBook);
                });
    }

    // Book 객체 생성
    private Book createBookFromSelectBook(SelectedBook book,User user){
        return Book.builder()
                .bookTitle(book.bookTitle())
                .description(book.description())
                .imageUrl(book.imageUrl())
                .author(book.author())
                .publisher(book.publisher())
                .isbn(book.isbn())
                .createdAt(LocalDate.now())
                .isLiked(false) //default=false
                .user(user)
                .build();
    }
    // SaveSelectedBookResponse 객체 생성
    private SaveSelectedBookResponse createSaveSelectedBookResponse(Book book){
        return new SaveSelectedBookResponse(
                "책을 성공적으로 저장했습니다.",
                book.getBookTitle(),
                book.getDescription(),
                book.getImageUrl(),
                book.getAuthor(),
                book.getPublisher(),
                book.getIsbn()
        );
    }
}
