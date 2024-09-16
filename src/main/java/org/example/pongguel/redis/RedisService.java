package org.example.pongguel.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService implements RedisRepository{
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String getRedis(RedisParam param) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        String result = (String) operations.get(param.key());
        if (!StringUtils.hasText(result)) {
            operations.set(param.key(), param.value(), 7, TimeUnit.DAYS); // 7일 동안 저장
            log.info("redis save");
            result = param.value();
        }
        return result;
    }
}
