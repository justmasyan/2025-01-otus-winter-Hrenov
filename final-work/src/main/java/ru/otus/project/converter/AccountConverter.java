package ru.otus.project.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.project.domain.Account;
import ru.otus.project.dto.AccountDto;

@Component
@RequiredArgsConstructor
public class AccountConverter {

    private final AccountRoleConverter roleConverter;

    //проверить на n+1 на доп.запрос для роли
    public AccountDto entityToDtoWOPassword(Account account) {
        return new AccountDto(account.getId(), account.getUsername(),
                roleConverter.entityToDto(account.getAuthority()));
    }

    public AccountDto entityToBaseInfoDto(Account account) {
        return new AccountDto(account.getId(), account.getUsername());
    }

    public AccountDto entityToAuthorizationDto(Account account) {
        return new AccountDto(account.getId(), account.getUsername(), account.getPassword(),
                roleConverter.entityToDto(account.getAuthority()));
    }

    public AccountDto entityToDto(Account account) {
        return new AccountDto(account.getId(), account.getUsername(),
                roleConverter.entityToDto(account.getAuthority()));
    }

    public Account dtoToEntity(AccountDto accountDto) {
        return new Account(accountDto.getId(), accountDto.getUsername(),
                accountDto.getPassword(),
                roleConverter.dtoToEntity(accountDto.getAuthority()));
    }
}
