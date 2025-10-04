package ru.otus.project.service;

import ru.otus.project.dto.AccountDto;

public interface AccountService {

    AccountDto insert(AccountDto accountDto);

    AccountDto update(AccountDto accountDto);
}
