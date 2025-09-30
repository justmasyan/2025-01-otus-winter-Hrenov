package ru.otus.project.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.project.dto.AccountDto;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtAuthServiceImpl implements JwtAuthService {

    @Value("${app.security.secret}")
    private String secret;

    @Value("${app.security.lifetime}")
    private Duration lifetime;

    public String generateJwtToken(AccountDto accountDto) {
        Date issuedDate = new Date();
        Date expireDate = new Date(issuedDate.getTime() + lifetime.toMillis());

        return Jwts.builder()
                .subject(accountDto.getUsername())
                .claim("id", accountDto.getId())
                .claim("authorityId", accountDto.getAuthority().getId())
                .claim("authority", accountDto.getAuthority().getAuthority())
                .issuedAt(issuedDate)
                .expiration(expireDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build().parseSignedClaims(token).getPayload();
    }
}
