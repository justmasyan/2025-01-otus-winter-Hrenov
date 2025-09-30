package ru.otus.project.converter;

import org.springframework.stereotype.Component;
import ru.otus.project.domain.AccountRole;
import ru.otus.project.dto.AccountRoleDto;

@Component
public class AccountRoleConverter {

    public AccountRoleDto entityToDto(AccountRole role) {
        return new AccountRoleDto(role.getId(), role.getDescription(), role.getAuthority());
    }

    public AccountRole dtoToEntity(AccountRoleDto roleDto) {
        return new AccountRole(roleDto.getId(), roleDto.getDescription(), roleDto.getAuthority());
    }
}
