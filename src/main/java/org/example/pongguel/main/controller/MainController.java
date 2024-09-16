package org.example.pongguel.main.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/main")
@RestController
@RequiredArgsConstructor
@Tag(name="Main",description = "퐁글서비스의 메인 페이지 Api입니다.")
public class MainController {

    @GetMapping("/ponggeul")
    public ResponseEntity<String> ponggeul() {
        String welcomeMessage = "퐁글퐁글 서비스를 시작합니다!";
        return ResponseEntity.ok(welcomeMessage);
    }
}
