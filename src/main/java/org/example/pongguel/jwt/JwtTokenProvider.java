package org.example.pongguel.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JwtTokenProvider {

    @Value("${jwt.access_secret_key}")
    private String accessSecretKey;
    @Value("${jwt.refresh_secret_key}")
    private String refreshSecretKey;
    @Value("${jwt.share_secret_key}")
    private String shareSecretKey;


    private final RedisTemplate<String, String> redisTemplate;
    private final UserDetailsService userDetailsService;

    // 인코딩된 시크릿키 디코딩
    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // accessToken 생성
    public String createAccessToken(String email){
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        //30분
        long accessTokenExpiration = 30 * 60 * 1000L;
        Date expiration = new Date(now.getTime() + accessTokenExpiration);

        String accessToken =  Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(expiration)
                        .signWith(getSigningKey(accessSecretKey), SignatureAlgorithm.HS256)
                        .compact();

        log.debug("액세스 토큰이 발급되었습니다. 이메일: {}, 토큰: {}",email,accessToken);
        return accessToken;
    }
    // refreshToken 생성
    public String createRefreshToken(String email){
        Date now = new Date();
        long refreshTokenExpiration = 60 * 60 * 24 * 7 * 1000L;  //7일
        Date expiration = new Date(now.getTime() + refreshTokenExpiration);

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(refreshSecretKey), SignatureAlgorithm.HS256)
                .compact();

        redisTemplate.opsForValue().set(
                "RT:" + email,
                refreshToken,
                refreshTokenExpiration,
                TimeUnit.MILLISECONDS
        );
        log.debug("리프레시 토큰이 발급되었습니다. 이메일: {}", email);
        return refreshToken;
    }
    // shareToken 생성
    public String createShareToken(String email, Long bookId){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("bookId", bookId);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 600000); //10분

        String shareToken =  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(shareSecretKey), SignatureAlgorithm.HS256)
                .compact();

        log.debug("공유 토큰이 발급되었습니다. 공유한 이메일: {}, 토큰: {}",email,shareToken);
        return shareToken;
    }

    // 액세스, 리프레시 토큰에서 email 가져오기
    public String getUserEmail(String token, boolean isAccessToken){
        String secretKey = isAccessToken ? accessSecretKey : refreshSecretKey;
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    // 공유 토큰에서 bookId 추출하기
    public Long getBookIdFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey(shareSecretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("bookId", Long.class);
    }

    // 액세스 토큰 유효성 검사
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(accessSecretKey))
                    .setAllowedClockSkewSeconds(30) // 30초 오차범위
                    .build()
                    .parseClaimsJws(token);
            log.debug("액세스 토큰이 성공적으로 검증되었습니다.");
            return true;
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("잘못된 형식의 JWT 토큰: {}", e.getMessage());
        }
        return false;
    }
    // redis 리프레시 토큰 확인
    public boolean validateRefreshToken(String token){
        try{
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(refreshSecretKey))
                    .setAllowedClockSkewSeconds(30) // 30초 오차범위
                    .build()
                    .parseClaimsJws(token);
            String email = claims.getBody().getSubject();
            String storedToken = redisTemplate.opsForValue().get("RT:" + email);
            if (storedToken != null && storedToken.equals(token)) {
                log.debug("리프레시 토큰이 성공적으로 검증되었습니다. 사용자: {}", email);
                return true;
            } else {
                log.error("리프레시 토큰이 Redis에 없거나 일치하지 않습니다. 사용자: {}", email);
                return false;
            }
        } catch (JwtException | IllegalArgumentException e) {
            log.error("유효하지 않은 리프레시 토큰: {}", e.getMessage());
            return false;
        }
    }
    // shareToken 확인
    public boolean validateShareToken(String token){
        try{
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(shareSecretKey))
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("유효하지 않은 공유 토큰: {}", e.getMessage());
            return false;
        }
    }
    // 권한 확인
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token, true));
        log.debug("사용자 인증이 생성되었습니다. 사용자: {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
