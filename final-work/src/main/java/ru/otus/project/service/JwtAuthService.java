package ru.otus.project.service;

import ru.otus.project.dto.AccountDto;

public interface JwtAuthService {
    String generateJwtToken(AccountDto accountDto);
}
