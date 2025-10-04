package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private long id;

    private String firstName;

    private String lastName;

    private String studyPlace;

    private AccountDto account;

    public StudentDto(long id, String firstName, String lastName, String studyPlace) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studyPlace = studyPlace;
    }
}
