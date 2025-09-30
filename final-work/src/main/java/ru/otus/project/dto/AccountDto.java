package ru.otus.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto implements UserDetails {

    private long id;

    private String username;

    private String password;

    private AccountRoleDto authority;

    public AccountDto(long id, String username) {
        this.id = id;
        this.username = username;
    }

    public AccountDto(long id, String username, AccountRoleDto authority) {
        this.id = id;
        this.username = username;
        this.authority = authority;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }
}
