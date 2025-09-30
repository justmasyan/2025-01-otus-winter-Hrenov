package ru.otus.project.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.AccountRoleDto;
import ru.otus.project.service.JwtAuthServiceImpl;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtAuthServiceImpl authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
            String token = authHeader.substring(7);
            Claims tokenClaims = authService.getClaimsFromToken(token);
            AccountDto accountDto = new AccountDto(tokenClaims.get("id", Long.class), tokenClaims.getSubject(),
                    new AccountRoleDto(tokenClaims.get("authorityId", Long.class), tokenClaims.get("authority", String.class)));
            var user = new UsernamePasswordAuthenticationToken(accountDto, null, List.of(accountDto.getAuthority()));
            SecurityContextHolder.getContext().setAuthentication(user);
        }
        filterChain.doFilter(request, response);
    }
}
