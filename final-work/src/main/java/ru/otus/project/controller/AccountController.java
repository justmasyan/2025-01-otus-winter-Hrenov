package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.AuthRequestDto;
import ru.otus.project.dto.AuthResponseDto;
import ru.otus.project.service.JwtAuthService;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final JwtAuthService authenticationService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("api/account/authentication")
    public ResponseEntity<?> getAuthenticationToken(@RequestBody AuthRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
        );
        String token = authenticationService.generateJwtToken((AccountDto) authentication.getPrincipal());
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
