package ru.otus.project.service;

import ru.otus.project.dto.AccountRoleDto;

import java.util.List;


public interface AccountRoleService {

    List<AccountRoleDto> findAll();
}
