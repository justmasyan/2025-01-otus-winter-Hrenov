package ru.otus.project.dto;

import lombok.Data;

@Data
public class AuthRequestDto {

    private String username;

    private String password;

    //TODO: сделать BeanValidation, чтобы поля точно были
}
