package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.project.converter.AccountRoleConverter;
import ru.otus.project.dto.AccountRoleDto;
import ru.otus.project.repository.AccountRoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountRoleServiceImpl {

    private final AccountRoleRepository roleRepository;

    private final AccountRoleConverter roleConverter;

    public List<AccountRoleDto> findAll() {
        return roleRepository.findAll().stream()
                .map(roleConverter::entityToDto).toList();
    }
}
