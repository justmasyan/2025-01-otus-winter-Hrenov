package ru.otus.hw.models;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import ru.otus.hw.converters.UserRoleConverter;
import ru.otus.hw.security.UserRole;

@Data
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String login;

    @Column
    private String password;

    @Column
    @Convert(converter = UserRoleConverter.class)
    private UserRole role;
}
