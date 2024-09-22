## PONGGEUL(퐁글퐁글) 🫧📚
> 원티드 프리온보딩 백엔드 인턴십에서 기술 과제로 요구사항을 받고 개인으로 진행한 프로젝트입니다. 단, 해당 프로젝트는 자유롭게 해석 및 구현이 가능했기 때문에 가상의 유저들이 지속적인 책 공유와 노트 생성을 위한 목적을 가지고 일부 재해석하여 구현했습니다.

<br/>

## 목차
#### [1. 개요](#개요)
##### [&nbsp;&nbsp;1-1. 실행 환경](#실행-환경)
##### [&nbsp;&nbsp;1-2. 기술 스택](#기술-스택)
##### [&nbsp;&nbsp;1-3. 프로젝트 관리](#프로젝트-관리)
#### [2. ERD 및 디렉토리 구조](#ERD-및-디렉토리-구조)
##### &nbsp;&nbsp;2-1. ERD
##### &nbsp;&nbsp;2-2. 디렉토리 구조
#### [3. 기능구현](#기능구현)
#### [4. API 명세](#API-명세)
#### [5. 트러블 슈팅](#트러블-슈팅)
#### [6. 고민의 흔적](#고민의-흔적)
#### [7. Git Commit Message Convention](#Git-Commit-Message-Convention)
#### [8. Git Branch 전략](#Git-Branch-전략)

</br>

## 개요
* "전에 재미있게 읽던 책이 뭐였지?"</br>
* "책별로 짧게 노트를 적어 감상문을 정리해놓고 싶다!"</br>
* "친구들이 꼭 읽어줬으면 좋겠는데... 감상과 함께 어떻게 알려주지?"</br>
* **퐁글퐁글에서 위 고민들 전부 해결해보세요!!!**

### 실행 환경
* application-secret.yml 환경변수 파일 생성</br>
해당 프로젝트는 아래 항목들이 환경변수 파일에 전부 존재해야 합니다.

  <details>
  <summary><strong>secret.yml</strong></summary>
  
  ```
  spring:
    data:
      redis:
        host: 
        port: 
  
  kakao:
    client_id: 
    redirect_url:
    logout_redirect_url: #메인페이지
    user_base_url: 
    kauth_base_url: 
    user_info_url: 
    send_base_url: 
    send_me_url: 
    user_friend_url: 
    send_friends_url: 
  
  jwt:
    access_secret_key: 
    refresh_secret_key: 
    share_secret_key: 
  
  naver:
    client_id: 
    client_secret: 
    book_base_url: o
  
  share_book:
    share_base_url: 
  
  ```
  </details>

### 기술 스택
![Java](https://img.shields.io/badge/-Java-FF7800?style=flat&logo=Java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-flat&logo=spring&logoColor=white)
![SpringBoot](https://img.shields.io/badge/-SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white)
![SpringDataJPA](https://img.shields.io/badge/SpringDataJpa-236DB33F?style=flat&logo=spring&logoColor=white)
![SpringSecurity](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat&logo=Spring%20Security&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-FF4438?style=flat&logo=Redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white)



### 프로젝트 관리
![Notion](https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-5865F2?style=flat&logo=Discord&logoColor=white)
</br> 
+ 프로젝트 시작 전 만들어야 할 API를 노션 보드에 전체와 서비스별 티켓으로 작성하고</br> 
+ 타켓마다 `MoSCoW` 방식으로 우선순위를 정하며, 티켓마다 이슈 생성하였고</br>
+ PR 생성할 때, 변경사항과 테스트 결과를 작성하고 머지 진행</br>
+ 디스코드 webhook을 활용해 PR 관련 알림을 확인하고 다음 작업 진행

  <details>
  <summary><strong>일정 관리</strong></summary>
  <div markdown="1">
  
  </br>
  <details>
  <summary>전체 일정 관리</summary>
  <div markdown="1">
  <img width="447" alt="전체 일정 관리" src="https://github.com/user-attachments/assets/5b89565e-c1bd-4f3f-9986-cb8c67cf1f6c" width="90%" height="100%">
  
  </div>
  </details>
  
  <details>
  <summary>사용자</summary>
  <div markdown="1">
  <img width="449" alt="사용자" src="https://github.com/user-attachments/assets/c1d1f1ce-9d8e-42ed-80e9-8a578f8eaf9f" width="90%" height="100%">
  </div>
  </details>
  
  <details>
  <summary>책</summary>
  <div markdown="1">
  <img width="456" alt="책" src="https://github.com/user-attachments/assets/beea6fed-8c9c-495b-9461-79450d11bcae" width="90%" height="100%">
  </div>
  </details>
  
  <details>
  <summary>노트</summary>
  <div markdown="1">
  <img width="451" alt="노트" src="https://github.com/user-attachments/assets/41ded673-84b9-46db-88ae-810aedf2dae4" width="90%" height="100%">
  </div>
  </details>
  
  </div>
  </details>
  
  <details>
  <summary><strong>이슈 관리</strong></summary>
  <div markdown="1">
  <img src="https://github.com/user-attachments/assets/e8b2574c-7af8-4a8b-ab65-b9de3f4d8942" width="90%" height="100%">    
  </div>
  </details>
  
  <details>
  <summary><strong>PR 관리</strong></summary>
  <div markdown="1">
  <img src="https://github.com/user-attachments/assets/9fdf5590-e62e-4a13-bbbd-72eb52bfda2d" width="90%" height="100%">  
  </div>
  </details>
  
  <details>
  <summary><strong>PR 변경사항 작성</strong></summary>
  <div markdown="1">
  <img src="https://github.com/user-attachments/assets/a00004c7-6922-43ad-9743-34d74ba2317e" width="90%" height="100%">  
  </div>
  </details>
  
  
  <details>
  <summary><strong>PR 알림</strong></summary>
  <div markdown="1">
  <img width="1155" alt="디스코드 알림" src="https://github.com/user-attachments/assets/fa3dbce2-04ba-4107-acd7-a2f2bc2798f3" width="90%" height="100%">
  
  </div>
  </details>

</br>

## ERD 및 디렉토리 구조

<details>
<summary><strong>ERD</strong></summary>
<div markdown="1">
 
<img src="https://github.com/user-attachments/assets/bdcc89d8-592c-4240-a74f-f8037b27b2ab" width="90%" height="100%" >

</div>
</details>

<details>
<summary><strong>디렉토리 구조</strong></summary>
<div markdown="1">
 
```bash
    ├── main
    │   ├── java
    │   │   └── org
    │   │       └── example
    │   │           └── pongguel
    │   │               ├── PongguelApplication.java
    │   │               ├── book
    │   │               │   ├── controller
    │   │               │   │   ├── BookController.java
    │   │               │   │   ├── BookLikeController.java
    │   │               │   │   ├── BookSaveController.java
    │   │               │   │   └── BookShareController.java
    │   │               │   ├── domain
    │   │               │   │   └── Book.java
    │   │               │   ├── dto
    │   │               │   │   ├── BookDetailWithNoteListResponse.java
    │   │               │   │   ├── DeleteSavedBookResponse.java
    │   │               │   │   ├── LikeBookResponse.java
    │   │               │   │   ├── LikedBookListResponse.java
    │   │               │   │   ├── SaveSelectedBookResponse.java
    │   │               │   │   ├── SavedBookListResponse.java
    │   │               │   │   ├── SearchBookList.java
    │   │               │   │   ├── SearchBookRequest.java
    │   │               │   │   ├── SearchBookResponse.java
    │   │               │   │   ├── SelectedBook.java
    │   │               │   │   ├── ShareBookResponse.java
    │   │               │   │   └── SharedBookDetailWithNoteListResponse.java
    │   │               │   ├── repository
    │   │               │   │   └── BookRepository.java
    │   │               │   └── service
    │   │               │       ├── BookService.java
    │   │               │       ├── LikedBookService.java
    │   │               │       ├── SavedBookService.java
    │   │               │       ├── ShareBookByKakaoTalkService.java
    │   │               │       └── SharedBooksDetailService.java
    │   │               ├── config
    │   │               │   ├── ApiConfig.java
    │   │               │   ├── RedisConfig.java
    │   │               │   ├── SecurityConfig.java
    │   │               │   └── SwaggerConfig.java
    │   │               ├── exception
    │   │               │   ├── BadRequestException.java
    │   │               │   ├── BaseException.java
    │   │               │   ├── ConflictException.java
    │   │               │   ├── ErrorCode.java
    │   │               │   ├── ErrorResponse.java
    │   │               │   ├── ForbiddenException.java
    │   │               │   ├── InternalServerException.java
    │   │               │   ├── NotFoundException.java
    │   │               │   ├── UnauthorizedException.java
    │   │               │   └── handler
    │   │               │       └── GlobalExceptionHandler.java
    │   │               ├── feign
    │   │               │   ├── client
    │   │               │   │   └── SendKakaoTalkClient.java
    │   │               │   ├── dto
    │   │               │   │   ├── KakaoFriendsInfo.java
    │   │               │   │   ├── KakaoTalkFriendsSendRequest.java
    │   │               │   │   ├── KakaoTalkSendRequest.java
    │   │               │   │   └── KakaoTalkSendResponse.java
    │   │               │   └── service
    │   │               │       └── KakaoTalkService.java
    │   │               ├── jwt
    │   │               │   ├── JwtAuthenticationFilter.java
    │   │               │   ├── JwtTokenProvider.java
    │   │               │   ├── JwtUtil.java
    │   │               │   ├── controller
    │   │               │   │   └── JwtController.java
    │   │               │   ├── domain
    │   │               │   │   └── CustomUserDetails.java
    │   │               │   ├── dto
    │   │               │   │   ├── JwtTokenDto.java
    │   │               │   │   └── TokenRefreshRequest.java
    │   │               │   └── service
    │   │               │       ├── JwtService.java
    │   │               │       └── UserDetailService.java
    │   │               ├── main
    │   │               │   ├── controller
    │   │               │   │   └── MainController.java
    │   │               │   └── service
    │   │               ├── note
    │   │               │   ├── controller
    │   │               │   │   ├── NoteBookmarkController.java
    │   │               │   │   ├── NoteController.java
    │   │               │   │   └── NoteDetailController.java
    │   │               │   ├── domain
    │   │               │   │   └── Note.java
    │   │               │   ├── dto
    │   │               │   │   ├── BookmarkNoteResponse.java
    │   │               │   │   ├── BookmarkedNoteListResponse.java
    │   │               │   │   ├── NoteDeleteResponse.java
    │   │               │   │   ├── NoteDetailResponse.java
    │   │               │   │   ├── NoteListResponse.java
    │   │               │   │   └── NoteWriteRequest.java
    │   │               │   ├── repository
    │   │               │   │   └── NoteRepository.java
    │   │               │   └── service
    │   │               │       ├── NoteBookmarkService.java
    │   │               │       ├── NoteDeleteService.java
    │   │               │       ├── NoteDetailService.java
    │   │               │       └── NoteService.java
    │   │               ├── redis
    │   │               │   ├── RedisController.java
    │   │               │   ├── RedisParam.java
    │   │               │   ├── RedisRepository.java
    │   │               │   └── RedisService.java
    │   │               └── user
    │   │                   ├── controller
    │   │                   │   ├── KakaoReissueController.java
    │   │                   │   ├── KakaoSignInController.java
    │   │                   │   ├── KakaoSignOutController.java
    │   │                   │   └── UserDetailController.java
    │   │                   ├── domain
    │   │                   │   ├── KakaoToken.java
    │   │                   │   ├── Role.java
    │   │                   │   └── User.java
    │   │                   ├── dto
    │   │                   │   ├── KakaoTokenInfo.java
    │   │                   │   ├── KakaoUserInfoAndToken.java
    │   │                   │   ├── KakaoUserInfoResponse.java
    │   │                   │   ├── LoginResponse.java
    │   │                   │   ├── LoginResult.java
    │   │                   │   ├── PongUserInfo.java
    │   │                   │   └── UnlinkedUserDto.java
    │   │                   ├── repository
    │   │                   │   ├── KakaoTokenRepository.java
    │   │                   │   └── UserRepository.java
    │   │                   └── service
    │   │                       ├── KakaoSignInService.java
    │   │                       ├── KakaoSignOutService.java
    │   │                       ├── KakaoTokenReissueService.java
    │   │                       ├── UserService.java
    │   │                       └── ValidateUser.java
    │   └── resources
    │       ├── application-secret.yml
    │       ├── application.yml
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── org
                └── example
                    └── pongguel
                        └── PongguelApplicationTests.java
```
</div>
</details>

</br>

## 기능구현
### 카카오 로그인 및 회원가입 (Kakao 로그인 API)
* 카카오 이메일 중복체크
* 회원정보가 없을 시 회원가입 함께 진행
* 로그인 시 JWT(Json Web Token) 발급 -> 모든 API 요청시 JWT 인가

### 서비스 로그아웃
* 카카오 서비스 전체 로그아웃
* 로그아웃 시 Redis에 저장된 Refresh Token 삭제

### 마이페이지
* 사용자 마이페이지
* 회원 탈퇴 시 모든 정보 삭제 및 카카오 연결 끊기

### 책 조회 및 저장 (Naver 책 검색 API)
* 검색어는 자동으로 인코딩되어 전달
* 책 검색은 스프링 시큐리티 예외
* 사용자가 선택한 책의 ISBN을 바탕으로 책을 상세조회하고 DB에 저장

### 내 서재
* 저장된 책 `좋아요` 기능 
* 저장된 책 `상세보기` 기능 (책의 정보와 작성된 노트리스트 함께 조회)
* 저장된 책 `노트` 작성 기능
* 저장된 책 `삭제` 기능, 책과 관련된 모든 노트도 함께 삭제
* 저장된 책 `공유` 기능, 공유토큰 및 공유 URL 생성 및 카카오톡 메시지 전송
* `좋아요`, `노트` 전체 목록 조회
 
### 책 공유하기 (Kakao 메시지 API)
* 사용자에게 10분간 유효한 공유토큰 생성
* 공유URL을 포함해 카카오톡 메시지로 전달
* 공유된 책의 URL은 스프링 시큐리티 예외
* 공유된 책의 노트들도 함께 열람 가능
  
  <details>
  <summary><strong> 카카오톡 메시지 보내기</strong></summary>
    <div markdown="1">
    <img src="https://github.com/user-attachments/assets/67d5fd0c-3ca7-4180-9237-2fa6f90133f2" width="80%" height="100%">
  </details>

### 노트
* 노트는 최신순으로 정렬
* 저장한 책에 대해서 제한없이 노트 생성
* 사용자는 노트 수정, 휴지통 이동(softDelete), 영구 삭제, 즐겨찾기(북마크) 가능
* 즐겨찾기한 노트들 전체 조회 (보관함으로 이동한 노트들은 제외)
* 전체 노트목록 (활성화, 휴지통) 조회
* 휴지통으로 이동한 노트는 30일 동안 언제든지 활성화 가능, 30일 뒤 영구 삭제

</br>

## API 명세
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=Swagger&logoColor=white)
|No| Title           | Method  | Path                       | Authorization |
|---|-----------------|:-------:|----------------------------|:-------------:|
|1|카카오 인가코드|`GET`|`/api/kakao/sign-in`|X|
|2|로그인 및 회원가입|`GET`|`/api/kakao/callback?code={카카오_인가코드}`|X|
|3|로그아웃|`GET`|`/api/kakao-logout/sign-out`|X|
|4|마이페이지|`GET`|`/api/users/my-page`|O|
|5|회원 탈퇴|`GET`|`/api/users/leave`|O|
|6|책 검색|`GET`|`/api/books/search`|X|
|7|책 저장|`POST`|`/api/books/save`|O|
|8|내 서재 조회|`GET`|`/api/saved-books/list`|O|
|9|서재 책 상세 조회|`GET`|`/api/saved-books:bookId/detail`|O|
|10|서재 책 삭제|`DELETED`|`/api/saved-books/:bookId/deleted`|O|
|11|서재 책 좋아요|`PATCH`|`/api/liked-books/:bookId/like`|O|
|12|서재 책 나에게 공유하기|`POST`|`/api/saved-books/:bookId/share`|O|
|13|서재 책 친구에게 공유하기|`POST`|`/api/saved-books/:bookId/share-friends`|O|
|14|외부인 책 상세 조회|`GET`|`/api/shared/books/:shareToken`|O|
|15|외부인 책 노트 상세 조회|`GET`|`/api/shared/books/:shareToken/notes/:noteId`|O|
|16|노트 생성|`POST`|`/api/notes/:bookId/write`|O|
|17|노트 상세 조회|`GET`|`/api/notes-detail/:noteId/read`|O|
|18|노트 수정|`PATCH`|`/api/notes-detail/:noteId/update`|O|
|19|노트 보관함 이동|`PATCH`|`/api/notes-detail/:noteId/soft-delete`|O|
|20|노트 삭제|`DELETED`|`/api/notes-detail/:noteId/final-delete`|O|
|21|노트 즐겨찾기|`PATCH`|`/api/notes-detail/{noteId}/read`|O|
|22|노트 즐겨찾기 목록 조회|`GET`|`/api/bookmarked-notes/list`|O|
|23|노트 활성화 목록 조회|`GET`|`/api/notes/active-list`|O|
|24|노트 휴지통 목록 조회|`GET`|`/api/notes/active-list`|O|

</br>

## 트러블 슈팅
<details>
<summary><strong>⚡ WebClient에서 JSON 응답 파싱 문제 </strong></summary>
<div markdown="1">
<p>

## 문제 상황
카카오 API에서 사용자 정보를 가져올 때, JSON 응답을 파싱하는 과정에서 get() 메서드 사용 시 문제가 발생
## 원인 분석
- JSON 구조의 복잡성: 카카오 API 응답의 JSON 구조가 중첩되어 있어 get() 메서드로 직접 접근하기 어려움
- Null 값 처리: get() 메서드는 키가 존재하지 않을 경우 예외를 발생시킬 수 있어, 안전한 처리가 필요

## 해결 과정
- path() 메서드 사용: JSON 트리를 안전하게 탐색할 수 있는 path() 메서드로 전환
- 중첩된 구조 처리: path().path() 형식으로 중첩된 JSON 구조를 효과적으로 처리

  ```
  {
  Long id = jsonNode.path("id").asLong();
  String email = jsonNode.path("kakao_account").path("email").asText();
  String nickname = jsonNode.path("kakao_account").path("profile").path("nickname").asText();
  String thumbnail_image_url = jsonNode.path("kakao_account").path("profile").path("thumbnail_image_url").asText();
  }
  ```
## 결과
- path() 메서드를 적절히 사용하여 JSON 데이터를 안전하게 파싱
- Null 안전성 확보: path() 메서드는 키가 없을 경우 MissingNode를 반환하여 NPE를 방지
- 기본값 처리: asText(), asLong() 등의 메서드는 해당 노드가 없거나 변환할 수 없는 경우 각각의 기본값(빈 문자열, 0 등)을 반환

</p>
</div>
</details>

<details>
<summary><strong>⚡ WebClient 네이버 책 검색 baseUrl 인식 문제</strong></summary>
<div markdown="1">
<p> 
 
## 문제 상황
WebClient의 Naver 책 관련 baseUrl에 전체 URL("https://openapi.naver.com/v1/search")이 설정되어 있었지만, </br>
실제 요청 시 이 baseUrl이 제대로 인식되지 않음

## 원인 분석
- WebClient에서 baseUrl을 설정했음에도 불구하고, uri() 메소드를 사용할 때 완전한 URI를 구성하지 않으면 baseUrl이 무시 가능성 있음
- 특히 UriBuilder를 사용할 때 이런 문제가 발생

## 해결 과정
- uriBuilder에 scheme("https")를 명시적으로 추가
- 중첩된 구조 처리: path().path() 형식으로 중첩된 JSON 구조를 효과적으로 처리

  ```
  {
    .uri(uriBuilder -> uriBuilder
    .scheme("https")  // 명시적으로 scheme 추가
    .host(bookBasehUrl)
    .path("/book.json")
    // ... 나머지 코드
  }
  ```
## 결과
- 완전한 URL 구성: scheme을 명시적으로 지정함으로써, 전체 URL이 올바르게 구성
  
</p>
</div>
</details>

<details>
<summary><strong>⚡ Feign JSON 직렬화 문제 및 파싱 오류</strong></summary>
<div markdown="1">
<p> 

## 문제 상황
책 공유하기를 진행하던 중 JSON 직렬화 문제가 발생했다.</br>
더불어 친구에게 공유하기는 사용자 친구 목록에서 불러온 UUID가 reqeust에 파싱되지 않아 `Bad request`가 계속 발생했다.

## 원인 분석
- JSON 직렬화 문제: 기존에 사용하던 record 타입이 Kakao API의 요구사항과 맞지 않았다.
- UUID 파싱 오류: 친구 목록의 UUID를 List 형태로 한 번에 전송하는 방식에 문제가 발생했다.
- UUID NULL 인식 오류


## 해결 과정

1. JSON 직렬화 문제
   
- 기존 record 타입을 Class 형식으로 다시 변경
- kakao 요구에 맞도록 클래스 구조 수정
- ObjectMapper를 활용해 json으로 직접 파싱해


2. UUID 파싱 오류
   
- 친구 목록 UUID를 List로 한 번에 전송하는 대신, 개별적으로 받아 처리하는 방식으로 변경


3. UUID BAD REQUEST 문제
   
- ReqeustBody의 순서 문제
  - 친구에게 전송하기 reqeust는 나에게 전송하기를 상속받아 진행했다.
  - 이 과정에서 uuid 리스트와 contents가 서로 순서가 바뀌었다.
  - feignClient에서는 각각 받지 않고 하나의 Map으로 묶어 응답 본문을 전송하고 있었다.
  - 계속해서 uuid가 null로 입력돼서 결국 하나씩 분리해 param 형태로 전송했다.

</p>
</div>
</details>

<details>
<summary><strong>⚡ JWT 토큰 유효기간 만료 오류</strong></summary>
<div markdown="1">
<p> 

## 문제 상황
액세스 토큰 유효기간 만료 오류
## 원인 분석
- application-secret.yml에 토큰별 유효기간을 Long 타입으로 정의했으나, 애플리케이션에서 이 값을 너무 짧게 인식하는 문제가 발생
- 더불어 Local 환경 시간과 JWT 환경 시간의 차이가 발생하는 오류도 발견
- 액세스 토큰 검증 30초 여유 시간을 설정해줬으면에도 여전히 문제가 발생

## 해결 과정
- 결국 하드 코딩을 유효기간 설정

## 다른 방법으로 해결법 고민하기
- YAML에서 Long 대신 Integer를 사용하기
- 문자열로 정의하고 JAVA로 파싱하기
      
```
jwt:
  token-expiration:
    access: "유효시간"
    refresh: "유효시간"
    share: "유효시간"
```

```
@Value("${jwt.access-token-expiration}")
private String accessTokenExpirationStr;
@Value("${jwt.refresh-token-expiration}")
private String refreshTokenExpirationStr;
@Value("${jwt.share-token-expiration}")
private String shareTokenExpirationStr;

private long getAccessTokenValidity() {
        return Long.parseLong(accessTokenExpirationStr);
    }

    private long getRefreshTokenValidity() {
        return Long.parseLong(refreshTokenExpirationStr);
    }

    private long getShareTokenValidity() {
        return Long.parseLong(shareTokenExpirationStr);
    }
```


</p>
</div>
</details>



</br>

## 고민의 흔적
<details>
<summary><strong>📝 RestTemplate에서 WebClient로의 전환</strong></summary>
<div markdown="1">
<p>

대부분 RestTemplate로 OpenAPI 응답값을 받아오곤 했었다.</br>
하지만 기본적으로 RestTemplate은 응답값을 받아오는 데 시간이 오래 걸릴 수도 있는 점이 가장 큰 문제였다. </br>
그리고 RestTemplate는 Spring에서 지원 중단을 예고했고, 새로운 개발은 대부분 WebClient로 많이 구현되고 있었다. </br>
특히 WebClient는 비동기 처리와 함수형 프로그래밍 스타일을 지원해 코드의 가독성과 유지보수성이 좋다고 알려져 있어, </br>
새로운 spring 라이브러리를 사용해보고 싶었다! </br>

WebClient의 비동기 특성을 활용하면 전체 애플리케이션의 응답성을 크게 향상시킬 수 있다는 점이 매력적이었다 </br>
로그인과 책 검색의 경우 빠른 응답이 필요했기에 적합한 방식이라는 생각이 들었다. </br>

WebClient 도입 시 초기 설정에서 RestTemplate과 약간의 차이가 있었다. </br>
JSON 인코딩 문제를 config 파일에 미리 설정해야 했다. 많은 블로그들에서 이에 관해 미리 말씀해주셔서 나는 다행히 초기 설정에 넣을 수 있었다. </br>

그리고 함수형 프로그래밍 스타일로 인해 코드가 간결해지고 가독성이 좋아졌다고 느겼다. </br>
또한 응답에서 원하는 정보만 쉽게 경로를 찾아 추출할 수 있다는 점이 좋았다. </br>
특히 타임아웃 기능이 인상적이었는데, 이를 통해 처리 속도를 더 세밀하게 제어할 수 있어 서비스 품질 향상에 도움이 될 것으로 기대된다! </br>
결과적으로 WebClient의 비동기 특성을 활용함으로써 전체 서버의 응답이 다른 프로젝트 때보다 빨라짐을 알 수 있었다.

</p>
</div>
</details>

<details>
<summary><strong>📝 클라이언트가 없을 때 카카오 로그인 인가코드를 어떻게 받아올까?</strong></summary>
<div markdown="1">
<p>
카카오 로그인의 대부분 구현은 클라이언트에게서 인가코드를 받아오고, </br>
서버에서 인가코드를 바탕으로 accessToken을 발급받는 것으로 진행된다.</br>

하지만 현재 백엔드 개발만 진행하고 있어서 이 부분을 서버 혼자서 처리할 순 없을까? 고민하게 됐다.</br>
우선, 인가코드는 동적으로 계속 변화하기 때문에 인가코드를 param으로 사용하자고 생각했다.</br>
그리고 리다이렉트 받는 url에서 바로 카카오 accessToken을 발급받고 회원정보까지 가져올 수 있도록 구현하게 됐다!</br>

이 방법을 외부인 책 접근에서도 활용할 수 있었다. 현재는 Url에 토큰과 인가코드가 전부 나오기 때문에 개인정보와 토큰 탈취 문제가 고민됐다.</br>
공유토큰의 시크릿키를 따로 분리해주고, url에는 보이지 않게 하는 방법을 좀 더 고민해 봐야겠다.</br>
</p>
</div>
</details>

<details>
<summary><strong>📝 카카오톡 메시지 공유하기를 클라이언트 없이 구현할까?</strong></summary>
<div markdown="1">
<p>
 
초기 요구사항에서는 카카오톡 메시지 공유하기는 따로 없었다.</br>
"디스코드 웹훅을 사용하는데, 내 서비스도 이런 기능이 있으면 좋겠는데?"에 대한 고민들이 하던 도중</br>
카카오톡 로그인을 서비스를 제공하니 카카오톡 공유하기와 카카오톡 메시지 전송하기가 떠올랐다.</br>

해당 API 명세서를 확인해보니까 Restful 방식은 카카오톡 메시지 전송하기만 가능했다.</br>
친구에게 사용자가 전달하는 방식이니, 오히려 카카오톡 메시지 전송하기가 퐁글에 더 잘 맞는다는 생각이 들었다! </br>

#### 고민된 점들
1. 클라이언트 없이 서버에서 공유 메시지 내용 고정
2. 서버에서 고정 템플릿 사용 불가로 JSON 직접 파싱 필요
3. 토큰 관리: 카카오 액세스 토큰 관리
4. 친구 정보 활용: 카카오 API로 사용자 친구 정보 조회
   
### 사용한 방법
1. openFeign 방식을 활용하기로 했다!
2. 물론 기존 webClient 방식으로도 가능했지만, 훨씬 코드가 간결해지고 client 작업을 대신 해주기에 백엔드의 부담이 덜었다.
3. 카카오 토큰은 임시로 서버 db에 저장하는 방식을 택했다. -> 추후에 스케줄러를 돌려서 관리해줄 예정이다.
4. 메시지는 고정으로 서버에서 작성하기로 했다. 카카오 메시지 템플릿을 사용해도 괜찮았지만 책 표지를 함께 보여주고 싶어서였다!

#### 구현 결과
- 로컬 환경에서 초기 테스트 성공
<img src="https://github.com/user-attachments/assets/67d5fd0c-3ca7-4180-9237-2fa6f90133f2" width="80%" height="100%">

**메시지 전송 기능을 통해 다양한 기술적 도전을 극복하며 실용적인 기능을 성공적으로 구현하게 되었다!**


</p>
</div>
</details>

<details>
<summary><strong>📝 프론트 화면은 어떻게 구상할까?</strong></summary>
<div markdown="1">
<p>

퐁글퐁글 서비스는 서로 연결된 기능들이 많아서 쉽게 로직이 연결되지 않았다. </br>
더불어 정보가 등록되어 있을 경우 해당 내역들까지 넘길 필요가 있을까? 라는 생각이 들면서 고민을 하게 되었다.</br>
이전 프로젝트를 할 때 프론트도 같이 만들어봤던 경험을 바탕으로 피그마를 활용해 간단하게 UI를 구상했다. </br>

</br>

이전 개인 프로젝트 할 때 프론트도 같이 만들어봤던 경험 때문에 프론트를 먼저 생각해보고</br>
로직을 생각하는 순서로 바뀐 계기가 되었다. 이렇게 생각하게 되니까 이해하기도 쉽고 로직이 머릿속에 그려졌다.</br>
</p>
</div>
</details>

## ✉ Git Commit Message Convention

<details>
<summary>커밋 유형</summary>
<div markdown="1">
</br>
  <table>
    <tr>
      <th scope="col">커밋 유형</td>
      <th scope="col">의미</td>
    </tr>
    <tr>
      <td>feat</td>
      <td>새로운 기능 추가</td>
    </tr>
    <tr>
      <td>fix</td>
      <td>버그 수정</td>
    </tr>
    <tr>
      <td>docs</td>
      <td>문서 수정</td>
    </tr>
    <tr>
      <td>style</td>
      <td>코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우</td>
    </tr>
    <tr>
      <td>refactor</td>
      <td>코드 리팩토링</td>
    </tr>
    <tr>
      <td>test</td>
      <td>테스트 코드, 리팩토링 테스트 코드 추가</td>
    </tr>
    <tr>
      <td>chore</td>
      <td>패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore</td>
    </tr>
  </table>
  </br>
</div>
</details>

<details>
<summary>커밋 메세지 세부 내용</summary>
<div markdown="1">
</br>
&emsp;• 글로 작성하여 내용이 잘 전달될 수 있도록 할 것 </br></br>
&emsp;• 본문에는 변경한 내용과 이유 설명 (어떻게보다는 무엇 & 왜를 설명)</br>
&emsp;<div align="center" style="width:90%; height: 140px; border: 1px solid gray; border-radius: 1em; background-color: #AEADAB; color: black; text-align: left ">
&emsp;ex ) </br>
&emsp;refactor : 로그인 기능 변경 (title)</br>
&emsp;( 공 백 ) </br>
&emsp;기존 로그인 방식에서 ~~한 문제로 ~~한 방식으로 변경하였습니다. (content)
</br>
</br>
&emsp;feat : 로그인 기능 구현
</div>
</div>
</details>

</br>

## 🌿 Git Branch 전략
<details>
<summary>브렌치 명명 규칙</summary>
<div markdown="1">
</br>

`feat/기능명`

- ex)  feat/users_apply

</div>
</details>

<details>
<summary>브랜치 작성 방법</summary>
<div markdown="1">
</br>

- 브랜치는 기능 단위로 나눈다.
- feat 브랜치는 dev 브랜치에서 파생해서 만든다.
- PR을 통해 dev 브랜치에서 기능이 완성되면 main 브랜치로 merge 한다.

</br>

|이름|텍스트|
|----|-----|
|main|제품으로 출시될 수 있는 브랜치|
|dev|다음 출시 버전을 개발하는 브랜치|
|feat|기능을 개발하는 브랜치|

</div>
</details>

</br>
