package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.project.domain.Course;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectorDto {

    private long id;

    private String firstName;

    private String lastName;

    private String jobTitle;

    private AccountDto account;

    private List<CourseDto> courses;

    public LectorDto(long id, String firstName, String lastName, String jobTitle) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
    }
}
