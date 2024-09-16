package org.example.pongguel.redis;

import lombok.RequiredArgsConstructor;
import org.example.pongguel.user.service.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisController {
    private final RedisService redisService;
    private final JwtService jwtService;

    @GetMapping("/redis")
    public String getRedis(@RequestBody RedisParam param) {
        return redisService.getRedis(param);
    }
}
