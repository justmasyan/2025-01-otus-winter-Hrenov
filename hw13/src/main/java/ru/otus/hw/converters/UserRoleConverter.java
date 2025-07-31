package ru.otus.hw.converters;

import jakarta.persistence.AttributeConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.otus.hw.security.UserRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRole userRole) {
        return userRole.getCode();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer code) {
        return Arrays.stream(UserRole.values())
                .filter(role -> role.getCode().equals(code))
                .findFirst().orElse(UserRole.UNDEFINED);
    }

    public List<GrantedAuthority> convertToAuthorities(UserRole role) {
        return switch (role) {
            case ADMIN -> List.of(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_MANAGER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN"));
            case MANAGER -> List.of(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_MANAGER"));
            case USER -> List.of(
                    new SimpleGrantedAuthority("ROLE_USER"));
            default -> new ArrayList<>();
        };
    }
}
