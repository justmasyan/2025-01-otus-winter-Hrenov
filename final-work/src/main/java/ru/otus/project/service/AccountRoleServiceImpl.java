package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.project.converter.AccountRoleConverter;
import ru.otus.project.dto.AccountRoleDto;
import ru.otus.project.repository.AccountRoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountRoleServiceImpl implements AccountRoleService {

    private final AccountRoleRepository roleRepository;

    private final AccountRoleConverter roleConverter;

    @Override
    public List<AccountRoleDto> findAll() {
        return roleRepository.findAll().stream()
                .map(roleConverter::entityToDto).toList();
    }
}
