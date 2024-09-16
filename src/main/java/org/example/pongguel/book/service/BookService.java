package org.example.pongguel.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pongguel.book.dto.SearchBookList;
import org.example.pongguel.book.dto.SearchBookResponse;
import org.example.pongguel.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    @Value("${naver.client_id}")
    private String clientId;
    @Value("${naver.client_secret}")
    private String clientSecret;
    @Value("${naver.book_base_url}")
    private String bookBasehUrl;

    private final WebClient webClient;
    private final BookRepository bookRepository;

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
}
