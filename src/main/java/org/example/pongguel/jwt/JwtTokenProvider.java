package org.example.pongguel.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.access_secret_key}")
    private String secretKey;
    @Value("${jwt.access_expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh_expiration}")
    private long refreshTokenExpiration;

    private final RedisTemplate<String, String> redisTemplate;
    private final UserDetailsService userDetailsService;

    public String createAccessToken(String email){
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public String createRefreshToken(String email){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpiration);

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

        redisTemplate.opsForValue().set(
                "RT:" + email,
                refreshToken,
                refreshTokenExpiration,
                TimeUnit.MILLISECONDS
        );
        return refreshToken;
    }

    public String getUserEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    // 권한 확인
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
