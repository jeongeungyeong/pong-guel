package org.example.pongguel.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.user.dto.JwtTokenDto;
import org.example.pongguel.user.dto.TokenRefreshRequest;
import org.example.pongguel.user.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class JwtController {
    private final JwtService jwtService;

    @PostMapping("/refresh-access")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        try{
            JwtTokenDto newTokens = jwtService.reissueTokenByRefreshToken(request.refreshToken());
            return ResponseEntity.status(HttpStatus.CREATED).body(newTokens);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
