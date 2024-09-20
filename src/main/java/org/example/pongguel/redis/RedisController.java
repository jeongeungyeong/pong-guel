package org.example.pongguel.redis;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pongguel.jwt.service.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name="Redis",description = "Redis 관련 API입니다.")
public class RedisController {
    private final RedisService redisService;
    private final JwtService jwtService;

    @GetMapping("/redis")
    public String getRedis(@RequestBody RedisParam param) {
        return redisService.getRedis(param);
    }
}
