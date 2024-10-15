package org.example.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private static final String BLACKLIST_KEY_PREFIX = "blacklist:jwt:";


    private final RedisTemplate<String, String> redisTemplate;

    public void blacklistToken(String jti, long expirationTime) {
        redisTemplate.opsForValue().set(BLACKLIST_KEY_PREFIX + jti, "true", expirationTime, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_KEY_PREFIX + jti));
    }
}
