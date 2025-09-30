package ru.otus.project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private long id;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private String studyPlace;

    private AccountDto account;

    public StudentDto(long id, String firstName, String lastName, String studyPlace) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studyPlace = studyPlace;
    }
}
