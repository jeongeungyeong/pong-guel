package org.example.pongguel.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.exception.ErrorCode;
import org.example.pongguel.exception.UnauthorizedException;
import org.example.pongguel.jwt.JwtUtil;
import org.example.pongguel.user.dto.PongUserInfo;
import org.example.pongguel.user.dto.UnlinkedUserDto;
import org.example.pongguel.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name="MY_PAGE",description = "사용자 마이페이지에 관련된 Api입니다.")
public class UserDetailController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/my-page")
    @Operation(summary = "회원 마이페이지", description = "회원 마이페이지 서비스입니다.")
    public ResponseEntity<PongUserInfo> myPage(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        PongUserInfo pongUserInfo = userService.getUserInfo(token);
        return ResponseEntity.status(HttpStatus.OK).body(pongUserInfo);
    }

    @GetMapping("/leave")
    @Operation(summary = "회원 탈퇴", description = "서비스뿐만 아니라 카카오 연결도 끊기게 됩니다.")
    public ResponseEntity<UnlinkedUserDto> leave(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            throw new UnauthorizedException(ErrorCode.JWT_INVALID_TOKEN);
        }
        UnlinkedUserDto unlinkedUserDto = userService.leave(token);
        return ResponseEntity.status(HttpStatus.OK).body(unlinkedUserDto);
    }
}
