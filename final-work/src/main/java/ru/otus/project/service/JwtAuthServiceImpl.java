package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import ru.otus.project.dto.AccountDto;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtAuthServiceImpl implements JwtAuthService {

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    @Autowired
    private final JwtEncoder encoder;

    public String generateJwtToken(AccountDto accountDto) {
        Instant issued = Instant.now();
        Instant expire =  Instant.ofEpochSecond(issued.getEpochSecond() + lifetime.getSeconds());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(accountDto.getUsername())
                .issuedAt(issued)
                .expiresAt(expire)
                .claim("accountId", accountDto.getId())
                .claim("authority", accountDto.getAuthority().getAuthority())
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
