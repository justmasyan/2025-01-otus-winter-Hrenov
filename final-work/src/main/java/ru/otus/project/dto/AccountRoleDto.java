package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRoleDto implements GrantedAuthority {

    private long id;

    private String description;

    private String authority;

    public AccountRoleDto(long id, String authority) {
        this.id = id;
        this.authority = authority;
    }
}
