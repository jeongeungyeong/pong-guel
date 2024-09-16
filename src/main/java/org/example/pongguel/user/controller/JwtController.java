package org.example.pongguel.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="JWT",description = "JWT토큰 발급 관련 API입니다.")
public class JwtController {
    private final JwtService jwtService;

    @PostMapping("/refresh-access")
    @Operation(summary = "accessToken 재발급", description = "refreshToken을 바탕으로 AccessToken을 재발급합니다.")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        try{
            JwtTokenDto newTokens = jwtService.reissueTokenByRefreshToken(request.refreshToken());
            return ResponseEntity.status(HttpStatus.OK).body(newTokens);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
